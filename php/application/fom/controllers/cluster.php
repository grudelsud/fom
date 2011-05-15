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
		$this->load->model('Cluster_model');
		echo $this->Cluster_model->read( $id_query, 'json' );
	}

	/**
	 * show stats related to a specific bounding box
	 * example url: http://vampireweekend.local/fom/index.php/cluster/dzstat/45.80/12.70/47.40/15.00
	 * 
	 * @param number $swLat
	 * @param number $swLon
	 * @param number $neLat
	 * @param number $neLon
	 */
	function dzstat( $swLat, $swLon, $neLat, $neLon )
	{
		$this->load->model('Query_model');
		
		// find all the geo-clusters within the location box expressed by SW-NE parameters
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
			$stat_output['queries'][$cluster->id_query]['clusters'][$cluster->id_cluster] = $meta;
			
			if( empty( $time_clusters[$cluster->id_parent]) ) {
				$time_clusters[$cluster->id_parent]['num_posts'] = $meta['post_count'];
				$time_clusters[$cluster->id_parent]['num_clusters'] = 1;
			} else {
				$time_clusters[$cluster->id_parent]['num_posts'] += $meta['post_count'];
				$time_clusters[$cluster->id_parent]['num_clusters'] += 1;
			}
		}

		// fetch all the queries that generated this set of clusters and store their metadata in the output
		foreach( $stat_output['queries'] as $id_query => $clusters ) {
			$query = $this->Query_model->read( $id_query );
			$meta = json_decode( $query->meta, TRUE );
			$meta['t_start'] = $query->t_start;
			$meta['t_end'] = $query->t_end;
			$meta['t_granularity'] = $query->t_granularity;
			$meta['geo_granularity'] = $query->geo_granularity;
			$stat_output['queries'][$id_query]['meta'] = $meta;
		}
		
		$time_sequence = array();
		$time_values = array();
		$num_posts = array();
		$num_clusters = array();
		
		// fetch all the time-based super clusters and store a key array of timings and an array of stats with timing-based keys
		foreach( $time_clusters as $id_cluster => $tc_values ) {
			$this->db->where('id_cluster', $id_cluster);
			$cluster_query = $this->db->get('cluster');
			$cluster = $cluster_query->row();
			$meta = json_decode( $cluster->meta, TRUE );
			
			$time_sequence[] = $meta['endTime'];
			$time_values[$meta['endTime']] = $tc_values;
		}

		// sort the key array of timings, and create single stat arrays (number of posts and clusters) according to the new sorting
		arsort($time_sequence);
		foreach( $time_sequence as $time ) {
			$stat_output['dates'][$time] = $time_values[$time];
			$num_posts[] = $time_values[$time]['num_posts'];
			$num_clusters[] = $time_values[$time]['num_clusters'];
		}
		
		//  implode the arrays and create the google chart urls
		$url_posts  = 'https://chart.googleapis.com/chart?cht=bhs';
		$url_posts .= '&amp;chd=t:'.implode(',', $num_posts);
		$url_posts .= '&amp;chxt=x,y&amp;chxl=1:|'.implode('|', $time_sequence).'|';
		$url_posts .= '&amp;chco=30a8c0&amp;chf=bg,s,333333&amp;chg=10,100,1,5&amp;chs=400x300';

		$url_clusters  = 'https://chart.googleapis.com/chart?cht=bhs';
		$url_clusters .= '&amp;chd=t:'.implode(',', $num_clusters);
		$url_clusters .= '&amp;chxt=x,y&amp;chxl=1:|'.implode('|', $time_sequence).'|';
		$url_clusters .= '&amp;chco=30a8c0&amp;chf=bg,s,333333&amp;chg=10,100,1,5&amp;chs=400x300';

		$stat_output['charts']['chartUrlPosts'] = $url_posts;
		$stat_output['charts']['chartUrlClusters'] = $url_clusters;
		
		// say hallo to jason and grab a pint
		echo json_encode( $stat_output );
	}

	/*
	private function extEncode($values, $max = 4095, $min = 0){
		$extended_table = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-.';
		$chardata = 'e:';
		$delta = $max - $min;
		$size = (strlen($extended_table));
		foreach($values as $k => $v){
			if($v >= $min && $v <= $max){
				$first = substr($extended_table, round(($v - $min) / $size * $max / $delta),1);
				$second = substr($extended_table, ((($v - $min) % $size) * $max / $delta), 1);
				$chardata .= "$first$second";
			} else {
				$chardata .= '__'; // Value out of max range;
			}
		}
		return($chardata);
	}

	*/
	
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
		$url  = 'https://chart.googleapis.com/chart?cht=bhs';
		$url .= '&amp;chd=t:'.implode(',', $chart_data);
		$url .= '&amp;chxt=x,y&amp;chxl=1:|'.implode('|', $chart_label).'|';
		$url .= '&amp;chco=30a8c0&amp;chf=bg,s,333333&amp;chg=10,100,1,5&amp;chs=450x400';

		$stat_output->chartUrl = $url;
		echo json_encode( $stat_output );
	}

	function read_semantic( $id_parent )
	{
		$this->load->model('Cluster_model');
		echo $this->Cluster_model->read_semantic( $id_parent, 'json' );
	}
	
	function read_keywords( $id_parent )
	{
		$this->load->model('Cluster_model');
		echo $this->Cluster_model->read_keywords( $id_parent, 'json' );
	}
	
	function read_post( $id_post )
	{
		$this->load->model('post_model');
		echo $this->post_model->read_id( $id_post );
	}

	function read_link( $id_link )
	{
		// TODO: implement
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