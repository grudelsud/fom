<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Cluster: controls operations on clusters
* 
* @author TMA
* @version 1.0 2010-11-18
*/
class Cluster extends CI_Controller
{
	
	function __construct()
	{
		parent::__construct();
	}
	
	function read( $id_query )
	{
		// profiling! save some log in the database
		$id_user = $this->session->userdata('id_user');
		if( !$id_user ) {
			$id_user = 1;
		}
		$this->fom_logger->log($id_user, 'query_read', array('id_query'=>$id_query));
		
		$this->load->model('Cluster_model');
		echo $this->Cluster_model->read( $id_query, 'json' );
	}

	/**
	 * show stats related to a specific bounding box
	 * example url: http://vampireweekend.local/fom/index.php/cluster/dzstat/since=/until=/swLat=45.80/swLon=12.70/neLat=47.40/neLon=15.00
	 * 
	 * @param number $swLat
	 * @param number $swLon
	 * @param number $neLat
	 * @param number $neLon
	 * 
	 * returns a json structured as follows:
	 * 
	 * {
	 * 	charts: {
	 * 		chartUrlPosts: url,
	 * 		chartUrlClusters: url
	 * 	} 
	 * }
	 */
	function dzstat( $since, $until, $swLat, $swLon, $neLat, $neLon )
	{
		$since = strtotime( preg_replace('/c_since=/', '', $since) );
		$until = strtotime( preg_replace('/c_until=/', '', $until) );
		$swLat = preg_replace('/swLat=/', '', $swLat);
		$swLon = preg_replace('/swLon=/', '', $swLon);
		$neLat = preg_replace('/neLat=/', '', $neLat);
		$neLon = preg_replace('/neLon=/', '', $neLon);
		
		// profiling! save some log in the database
		$id_user = $this->session->userdata('id_user');
		if( !$id_user ) {
			$id_user = 1;
		}
		$this->fom_logger->log($id_user, 'box_stat', array('swlat'=>$swLat, 'swLon'=>$swLon, 'neLat'=>$neLat, 'neLon'=>$neLon));
		
		$this->load->model('Query_model');
		
		// find all the geo-clusters within the location box expressed by SW-NE parameters (type = 2 means geo clusters!)
		
		// forget paramters regarding time granularity and intervals, these will be fetched later when selecting the originating queries
		// TODO FIXME: to improve performances, these parameters should be saved in the fom_cluster table
		$where_cond = array('type' => 2, 'lat >' => $swLat, 'lon >' => $swLon, 'lat <' => $neLat, 'lon <' => $neLon);
		$this->db->where( $where_cond );
		$cluster_query = $this->db->get('cluster');
		
		$stat_output = array();
		$time_clusters = array();
		
		// foreach cluster, save its meta in the output function, grouped by query id and create a stat vector $time_clusters containing 
		// the parent id (being a time-based super cluster), the number of clusters and the number of clustered posts within this location
		foreach( $cluster_query->result() as $cluster ) {
			$meta = json_decode( $cluster->meta, TRUE );
			$meta['post_count'] = count( explode(' ', $cluster->posts_meta) );
//			$stat_output['queries'][$cluster->id_query]['clusters'][$cluster->id_cluster] = $meta;
			
			if( empty( $time_clusters[$cluster->id_parent]) ) {
				$time_clusters[$cluster->id_parent]['num_posts'] = $meta['post_count'];
				$time_clusters[$cluster->id_parent]['num_clusters'] = 1;
			} else {
				$time_clusters[$cluster->id_parent]['num_posts'] += $meta['post_count'];
				$time_clusters[$cluster->id_parent]['num_clusters'] += 1;
			}
		}

		// fetch all the queries that generated this set of clusters and store their metadata in the output
//		foreach( $stat_output['queries'] as $id_query => $clusters ) {
//			$query = $this->Query_model->read( $id_query );
//			$meta = json_decode( $query->meta, TRUE );
//			$meta['t_start'] = $query->t_start;
//			$meta['t_end'] = $query->t_end;
//			$meta['t_granularity'] = $query->t_granularity;
//			$meta['geo_granularity'] = $query->geo_granularity;
//			$stat_output['queries'][$id_query]['meta'] = $meta;
//		}
		
		$time_sequence = array();
		$time_humanreadable = array();
		$time_values = array();
		$num_posts = array();
		$num_clusters = array();
		
		// fetch all the time-based super clusters and store a key array of timings and an array of stats with timing-based keys
		foreach( $time_clusters as $id_cluster => $tc_values ) {
			$this->db->where('id_cluster', $id_cluster);
			$cluster_query = $this->db->get('cluster');
			$cluster = $cluster_query->row();
			$meta = json_decode( $cluster->meta, TRUE );
			
			$c_since = strtotime( $meta['startTime'] );
			$c_until = strtotime( $meta['endTime'] );
			$c_diff = $c_until - $c_since;

			// get rid of anything out of the since/until boundaries and get only daily clusters
			if( $since < $c_since && $until > $c_until && 86400 == $c_diff ) {
				$time_sequence[] = $meta['endTime'];
				$time_humanreadable[] = date('D j.n.y H.i', strtotime($meta['endTime']));
				$time_values[$meta['endTime']] = $tc_values;
			}
		}

		$max_posts = 0;
		$max_clusters = 0;

//		$stat_output['time_clusters'] = $time_values;
		
		foreach( $time_sequence as $time ) {
//			$stat_output['dates'][$time] = $time_values[$time];

			$np = $time_values[$time]['num_posts'];
			$nc = $time_values[$time]['num_clusters'];

			$max_posts = $np > $max_posts ? $np : $max_posts;
			$max_clusters = $nc > $max_clusters ? $nc : $max_clusters;

			$num_posts[] = $np;
			$num_clusters[] = $nc;
		}

		//  implode the arrays and create the google chart urls
		$url_base  = 'http://chart.apis.google.com/chart';
		$url_base .= '?chf=bg,s,333333';	// background
		$url_base .= '&chxs=0,676767,11.5,1,_,676767|1,676767,11.5,1,l,676767';
		$url_base .= '&chxt=x,y';	// visible axis
		$url_base .= '&chbh=a';		// bar chart type
		$url_base .= '&chs=350x200';	// chart size
		$url_base .= '&cht=bhs';		// chart type = bars
		$url_base .= '&chdlp=t';		// legend position
		$url_base .= '&chma=|11';	// chart margins
		
		$url_posts = $url_base;
		$url_posts .= '&chdl=Number+of+Clustered+Posts';	// chart legend
		$url_posts .= '&chxl=1:|'.implode('|', array_reverse( $time_humanreadable ));
		$url_posts .= '&chxr=0,0,'.$max_posts.'|1,0,0';	// axis scale
		$url_posts .= '&chco=FF776B';	// bar colours
		$url_posts .= '&chd='.googlechart_extencode($num_posts);
		
		$url_clusters = $url_base;
		$url_clusters .= '&chdl=Number+of+Clusters';	// chart legend
		$url_clusters .= '&chxl=1:|'.implode('|', array_reverse( $time_humanreadable ));
		$url_clusters .= '&chxr=0,0,'.$max_clusters.'|1,0,0';	// axis scale
		$url_clusters .= '&chco=30A8C0';	// bar colours
		$url_clusters .= '&chd='.googlechart_extencode($num_clusters);

		$stat_output['charts']['chartUrlPosts'] = $url_posts;
		$stat_output['charts']['chartUrlClusters'] = $url_clusters;
		
		// say hallo to jason and grab a pint
		echo json_encode( $stat_output );
	}

	function stat( $id_query )
	{
		$this->load->model('Cluster_model');
		$this->load->model('Query_model');
		
		// container for json output, class properties will be expressed in java/javascript friendly notation (using capital letters, not underscores)
		$stat_output = new stdClass();

		$query = $this->Query_model->read( $id_query );
		
		$stat_output->tGranularity = $query->t_granularity;
		$stat_output->geoGranularity = $query->geo_granularity;

		// if query meta is json well formed, then it will push values inside the output container
		$query_meta = json_decode( $query->meta );
		if( !empty( $query_meta ) ) {
			$query_meta_array = get_object_vars( $query_meta );
			foreach( $query_meta_array as $key => $val ) {
				$stat_output->$key = $val;
			}
		}

		// profiling! save some log in the database
		$id_user = $this->session->userdata('id_user');
		if( !$id_user ) {
			$id_user = 1;
		}
		$this->fom_logger->log($id_user, 'query_stat', array('id_query'=>$id_query, 't_start'=>$query->t_start));
		
		$tf_idf = array();
		$geo_clusters = $this->Cluster_model->read( $id_query, 'object' );
		$count_semclusters = 0;
		
		// fetching term stats (sem_clusters) for each geo_cluster
		foreach( $geo_clusters as $geo_cluster ) {
			$sem_clusters = $this->Cluster_model->read_semantic( $geo_cluster->id_cluster, 'object' );
			$count_semclusters += count( $sem_clusters );

			foreach( $sem_clusters as $sem_cluster ) {
				$terms = explode(';', preg_replace('/\"/', '', $sem_cluster->terms_meta));
				$tf_idf = array_merge( $tf_idf, $terms );
			}
		}
		
		$stat_output->gClusterNumTot = count( $geo_clusters );
		$stat_output->sClusterNumTot = $count_semclusters;
		
		// creating TF-IDF array
		$tf_idf = array_count_values( $tf_idf );
		// maybe we can get rid of this using protovis?
		arsort( $tf_idf );

		$chart_data = array();
		$chart_label = array();
		
		$freq_bias = 5;
		$count_overbias = 0;

		// creating chart
		foreach( $tf_idf as $key => $val ) {
			$count_overbias ++;
			if( $val < $freq_bias ) {
				$stat_output->termsNumTot = count($tf_idf);
				$stat_output->termsNumOverbias = $count_overbias - 1;
				$stat_output->tfBias = $freq_bias;
				break;
			}
			$chart_data[] = $val;
			$chart_label[] = urlencode( $key );
		}

		$max = $chart_data[0];
		
		$stat_output->chartData = $chart_data;
		$stat_output->chartLabel = $chart_label;

		$url  = 'http://chart.apis.google.com/chart';
		$url .= '?chf=bg,s,333333';	// background
		$url .= '&chxl=1:|'.implode('|', array_reverse( $chart_label ));
		$url .= '&chxr=0,0,'.$max.'|1,0,0';	// axis scale
		$url .= '&chxs=0,676767,11.5,1,_,676767|1,676767,11.5,1,l,676767';
		$url .= '&chxt=x,y';	// visible axis
		$url .= '&chbh=a';		// bar chart type
		$url .= '&chs=450x400';	// chart size
		$url .= '&cht=bhs';		// chart type = bars
		$url .= '&chco=30A8C0';	// bar colours
		$url .= '&chd='.googlechart_extencode($chart_data);
		$url .= '&chdl=TF-IDF';	// chart legend
		$url .= '&chdlp=t';		// legend position
		$url .= '&chma=|11';	// chart margins
		$url .= '&chtt=Query+Stats';

		$stat_output->chartUrl = $url;
		echo json_encode( $stat_output );
	}

	/**
	 * called when a geo-cluster is selected, returns the list of semantic metadata associated to this geo-cluster
	 * 
	 * @param numeric $id_parent
	 */
	function read_semantic( $id_parent )
	{
		// profiling! save some log in the database
		$id_user = $this->session->userdata('id_user');
		if( !$id_user ) {
			$id_user = 1;
		}
		$this->fom_logger->log($id_user, 'cluster_readsemantic', array('id_parent'=>$id_parent));
		
		$this->load->model('Cluster_model');
		echo $this->Cluster_model->read_semantic( $id_parent, 'json' );
	}
	
	/**
	 * same as above function, but only displaying meta keywords and opengraph data (cluster type: 4)
	 * does not need to profile this request as it is always called after read_semantic
	 * 
	 * @param numeric $id_parent
	 */
	function read_keywords( $id_parent )
	{
		$this->load->model('Cluster_model');
		echo $this->Cluster_model->read_keywords( $id_parent, 'json' );
	}
	
	/**
	 * called when user selects one of the links in the post list, returns the content of the selected post
	 * 
	 * @param numeric $id_post
	 */
	function read_post( $id_post )
	{
		// profiling! save some log in the database
		$id_user = $this->session->userdata('id_user');
		if( !$id_user ) {
			$id_user = 1;
		}
		$this->fom_logger->log($id_user, 'cluster_readsemantic', array('id_post'=>$id_post));
		
		$this->load->model('post_model');
		echo $this->post_model->read_id( $id_post );
	}

	/**
	 * called when user selects one of the links in the post-related link list, returns the content of the selected link
	 * 
	 * @param numeric $id_link
	 */
	function read_link( $id_link )
	{
		// profiling! save some log in the database
		$id_user = $this->session->userdata('id_user');
		if( !$id_user ) {
			$id_user = 1;
		}
		$this->fom_logger->log($id_user, 'cluster_readsemantic', array('id_link'=>$id_link));

		$this->db->where('id_link', $id_link);
		$query = $this->db->get('link');
		if( $query->num_rows() > 0 ) {
			echo json_encode($query->row());
		} else {
			echo json_encode(new stdClass());
		}
	}
	
	function delete( $id_cluster )
	{
//		$this->load->model('Cluster_model');
//		$this->Cluster_model->delete( $id_cluster );
		echo json_encode( array('delete' => $id_cluster ) );
	}
}

/* end of file cluster.php */