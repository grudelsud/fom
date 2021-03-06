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
	*
	* parses a string formatted as in: ID1xID2xID3 and returns...
	*
	* @param Geocluster1xGeocluster2x... $list
	*/
	function read_list( $list )
	{
		// profiling! save some log in the database
		$id_user = $this->session->userdata('id_user');
		if( !$id_user ) {
			$id_user = 1;
		}
		$this->fom_logger->log($id_user, 'query_read_least', array('list'=>$list));
		$cluster_list = explode('x', $list);

		$clusters = array();
		foreach( $cluster_list as $id_cluster ) {
			$this->db->where('id_cluster', $id_cluster);
			$query = $this->db->get('cluster');
			$results = $query->result();

			$time_parent = -1;
			$time_meta = array();
				
			foreach( $results as $result ) {
				$cluster_values = json_decode( $result->meta, TRUE );
				$cluster_values['id_cluster'] = $result->id_cluster;
				$cluster_values['id_query'] = $result->id_query;
				// include post meta for geo clusters
				$cluster_values['posts_meta'] = $result->posts_meta;

				// TODO: optimize database structure, the following extra query to fecth just one date is highly inefficient
				// checking that id_parent is always the same is already saving a lot though
				if( $time_parent != $result->id_parent ) {
					$time_parent = $result->id_parent;
					$this->db->where('id_cluster', $result->id_parent);
					$query = $this->db->get('cluster');
					if( $query->num_rows() > 0 ) {
						$row = $query->row();
						$time_meta = json_decode( $row->meta, TRUE );
					}
				}
				$cluster_values = array_merge( $cluster_values, $time_meta );
				$clusters[] = $cluster_values;
			}
		}
		echo json_encode( $clusters );
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
		$url_base .= '?chf=bg,s,33333300';	// background
		$url_base .= '&chts=EFEFEF,11.5'; // title colour
		$url_base .= '&chxs=0,EEEEEE,11.5,1,_,EEEEEE|1,EEEEEE,11.5,1,l,EEEEEE';
		$url_base .= '&chxt=x,y';	// visible axis
		$url_base .= '&chbh=a';		// bar chart type
		$url_base .= '&chs=350x200';	// chart size
		$url_base .= '&cht=bhs';		// chart type = bars
		$url_base .= '&chdlp=t';		// legend position
		$url_base .= '&chma=|11';	// chart margins
		
		$url_posts = $url_base;
		$url_posts .= '&chdl=Clustered+Posts';	// chart legend
		$url_posts .= '&chxl=1:|'.implode('|', array_reverse( $time_humanreadable ));
		$url_posts .= '&chxr=0,0,'.$max_posts.'|1,0,0';	// axis scale
		$url_posts .= '&chco=FF776B';	// bar colours
		$url_posts .= '&chd='.googlechart_extencode($num_posts);
		
		$url_clusters = $url_base;
		$url_clusters .= '&chdl=Clusters';	// chart legend
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
		$tf = array();

		$geo_clusters = $this->Cluster_model->read( $id_query, 'object' );
		$count_semclusters = 0;
		
		$bias_termlength = 3;
		$bias_termcount = 6;
		$count_overbias = 0;

		// fetching term stats (sem_clusters) for each geo_cluster
		foreach( $geo_clusters as $geo_cluster ) {
			$sem_clusters = $this->Cluster_model->read_semantic( $geo_cluster->id_cluster, 'object' );
			$count_semclusters += count( $sem_clusters );

			foreach( $sem_clusters as $sem_cluster ) {
				$terms = json_decode( $sem_cluster->terms_meta, TRUE );
				
				foreach( $terms as $term => $val ) {
					if( strlen( $term ) > $bias_termlength ) {
						if( !isset( $tf_idf[$term] ) ) {
							$tf_idf[$term]['sum'] = $val;
							$tf_idf[$term]['count'] = 1;
						} else {
							$tf_idf[$term]['sum'] += $val;
							$tf_idf[$term]['count'] += 1;
						}
					}
				}
			}
		}
		
		$stat_output->gClusterNumTot = count( $geo_clusters );
		$stat_output->sClusterNumTot = $count_semclusters;
		
		foreach( $tf_idf as $term => $stat ) {
			if( $stat['count'] >= $bias_termcount ) {
				$count_overbias++;
				$tf[$term] = $stat['sum'] / $stat['count'];
			}
		}

		arsort( $tf );

		$stat_output->termsNumTot = count($tf_idf);
		$stat_output->termsNumOverbias = $count_overbias;
		$stat_output->tfBias = $bias_termcount;

		$stat_output->tf = $tf;
		echo json_encode( $stat_output );
	}
	
	function search_topic( $string )
	{
		// profiling! save some log in the database
		$id_user = $this->session->userdata('id_user');
		if( !$id_user ) {
			$id_user = 1;
		}
		$this->fom_logger->log($id_user, 'cluster_search_topic', array('string'=>$string));
		
		$query = $this->db->query('select * from fom_cluster where terms_meta LIKE \'%"'.$string.'%\'');
		$results = array();
		
		$riga = 0;
		
		foreach( $query->result() as $row ) {
			$meta = json_decode( $row->meta );
			$score = isset( $meta->score ) ? $meta->score : 0;

			$matches = array();
			$pattern = '("'.$string.'[a-z]*")';
			
			// fetch topics and scores
			preg_match_all($pattern, $row->terms_meta, $matches);
			foreach( $matches as $match ) {
				// TODO: check this out, might be a bug: why should it be empty?
				$key = isset( $match[0] ) ? $match[0] : 'empty';

				if( array_key_exists($key, $results)) {
					$results[$key]['score'] += $score;
					$results[$key]['parents'] .= ' '.$row->id_parent;
				} else {
					$results[$key]['score'] = $score;
					$results[$key]['parents'] = $row->id_parent;
				}
			}
			
			// calculate size and average, and create sortable array
			$sortable = array();
			foreach( $results as $key => $result ) {
				$parents = explode(' ', $result['parents']);
				$parents_count = count($parents);
				$results[$key]['count'] = $parents_count;
				$results[$key]['score_avg'] = $result['score'] / $parents_count;
				$sortable[$key] = $parents_count;
			}
			
			// recreate the output with the correct sorting method
			arsort( $sortable );
			$output = array();
			foreach( $sortable as $key => $value ) {
				$output[$key]['score'] = $results[$key]['score'];
				$output[$key]['parents'] = $results[$key]['parents'];
				$output[$key]['count'] = $results[$key]['count'];
				$output[$key]['score_avg'] = $results[$key]['score_avg'];
			}
		}
		echo json_encode( $output );
	}

	function search_topic_tf( $string, $context = 'all' )
	{
		// profiling! save some log in the database
		$id_user = $this->session->userdata('id_user');
		if( !$id_user ) {
			$id_user = 1;
		}
		$this->fom_logger->log($id_user, 'cluster_search_topic_tf', array('string'=>$string));

		$prefix = $this->db->dbprefix;
		
		switch( $context ) {
		case 'topics':
			$sql = 'select * from '.$prefix.'cluster where type = 3 AND terms_meta LIKE \'%'.$string.'%\'';
			break;
		case 'keywords':
			$sql = 'select * from '.$prefix.'cluster where type = 4 terms_meta LIKE \'%'.$string.'%\'';
			break;
		default:
			$sql = 'select * from '.$prefix.'cluster where terms_meta LIKE \'%'.$string.'%\'';
			break;
		}
		$query = $this->db->query( $sql );
		$results = array();

		$output = array();
		$pattern = '/^'.$string.'/';

		foreach( $query->result() as $row ) {
			$scores = json_decode( $row->terms_meta, TRUE );
			
			// fetch substring within terms_meta array and add score to results
			foreach ($scores as $term => $score) {
				if( preg_match($pattern, $term) ) {
					if( array_key_exists($term, $results)) {
						$results[$term]['score'] += $score;
						$results[$term]['parents'] .= ' '.$row->id_parent;
					} else {
						$results[$term]['score'] = $score;
						$results[$term]['parents'] = $row->id_parent;
					}
				}
			}
			
			// calculate size and average, and create sortable array
			$sortable = array();
			foreach( $results as $key => $result ) {
				$parents = explode(' ', $result['parents']);
				$parents_count = count($parents);
				$results[$key]['count'] = $parents_count;
				$results[$key]['score_avg'] = round( $result['score'] / $parents_count, 3 );
				$sortable[$key] = $parents_count;
			}
				
			// recreate the output with the correct sorting method
			arsort( $sortable );
			foreach( $sortable as $key => $value ) {
				$output[$key]['score'] = $results[$key]['score'];
				$output[$key]['parents'] = $results[$key]['parents'];
				$output[$key]['count'] = $results[$key]['count'];
				$output[$key]['score_avg'] = $results[$key]['score_avg'];
			}
		}
		echo json_encode( $output );
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

	function export_posts( $id_cluster, $format = 'json' )
	{
		// profiling! save some log in the database
		$id_user = $this->session->userdata('id_user');
		if( !$id_user ) {
			$id_user = 1;
		}
		$this->fom_logger->log($id_user, 'cluster_export_posts', array('id_cluster'=>$id_cluster));
		
		$this->load->model('Cluster_model');
		if( 'json' == $format ) {
			echo $this->Cluster_model->export_posts( $id_cluster, 'json' );
		} else {
			$posts = $this->Cluster_model->export_posts( $id_cluster, 'object' );
			$output = "date\tlat\tlon\tlang\tcontent\tuser_location\tuser_id\tfoll_count\ttweet_id\n";
			foreach( $posts as $post ) {
				$post_meta = json_decode( $post->meta );
				$output .= $post->created."\t";
				$output .= $post->lat."\t";
				$output .= $post->lon."\t";
				$output .= $post->lang."\t";
				$output .= $post->content."\t";
				$output .= $post->user_location."\t";
				$output .= $post_meta->twitterUserId."\t";
				$output .= $post_meta->followerCount."\t";
				$output .= $post_meta->tweetId."\n";
			}
			$this->load->helper('download');
			force_download('cluster'.$id_cluster.'_posts.csv', $output);
			echo $output;
		}
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