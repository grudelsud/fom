<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Cluster_model
*
* @author TMA
* @version 1.0 2010-11-22
*/
class Cluster_model extends CI_Model
{

	function __construct()
	{
		parent::__construct();
	}

	function create( $id_query, $start_t, $end_t, $tc_size, $gc_name, $lat_m, $lat_v, $lon_m, $lon_v, $gc_size, $sc_size, $terms )
	{
		// TODO: fix dull type checking
		if( is_numeric( $id_query ) ) {
			$meta = array(
				'start_t' => $start_t, 
				'end_t' => $end_t, 
				'tc_size' => $tc_size, 
				'gc_name' => $gc_name, 
				'lat_m' => $lat_m, 
				'lat_v' => $lat_v, 
				'lon_m' => $lon_m, 
				'lon_v' => $lon_v, 
				'gc_size' => $gc_size, 
				'sc_size' => $sc_size
				);

			$data = array(
				'id_query' => (empty($id_query) ? 1 : $id_query),
				'meta' => json_encode($meta),
				'terms_meta' => $terms
			);

			$this->db->insert('cluster', $data);
			return $this->db->insert_id();
		} else {
			return 0;
		}
	}

	function read( $id_query, $format = 'json' )
	{
		$this->db->where('type', 2);
		$this->db->where('id_query', $id_query);
		$query = $this->db->get('cluster');
		$results = $query->result();

		if( $format == 'json' ) {
			$clusters = array();
			foreach( $results as $result ) {
				$cluster_values = json_decode( $result->meta, TRUE );
				$cluster_values['id_cluster'] = $result->id_cluster;
				$cluster_values['id_query'] = $result->id_query;
				// include post meta for geo clusters
				$cluster_values['posts_meta'] = $result->posts_meta;
				$clusters[] = $cluster_values;
			}
			return json_encode( $clusters );
		} else {
			return $results;
		}
	}

	function export_posts( $id_cluster, $format = 'json' )
	{
		$this->db->where('id_cluster', $id_cluster);
		$query = $this->db->get('cluster');
		
		$result = array();
		if( $query->num_rows() > 0 ) {
			$row = $query->row();
			$posts = explode( ' ', $row->posts_meta );
			
			$this->load->model('Post_model');
			foreach( $posts as $post ) {
				$result[] = $this->Post_model->read_id( $post, 'object' );
			}
		}
		if( 'json' == $format ) {
			return json_encode( $result );
		} else {
			return $result;
		}	
	}

	function read_semantic( $id_parent, $format = 'json' )
	{
		$this->db->where('type', 3);
		$this->db->where('id_parent', $id_parent);
		$query = $this->db->get('cluster');
		$results = $query->result();

		if( $format == 'json' ) {
			$clusters = array();
			foreach( $results as $result ) {
				$cluster_values = json_decode( $result->meta, TRUE );
				
				$cluster_values['id_cluster'] = $result->id_cluster;
				$cluster_values['id_query'] = $result->id_query;
				// include terms_meta for semantic clusters
				$cluster_values['terms_meta'] = json_decode( $result->terms_meta, TRUE );
				$clusters[] = $cluster_values;
			}
			return json_encode( $clusters );
		} else {
			return $results;
		}
	}

	function read_keywords( $id_parent, $format = 'json' )
	{
		$this->db->where('type', 4);
		$this->db->where('id_parent', $id_parent);
		$query = $this->db->get('cluster');
		$results = $query->result();

		if( $format == 'json' ) {
			$clusters = array();
			foreach( $results as $result ) {
				$cluster_values = json_decode( $result->meta, TRUE );
				$cluster_values['id_cluster'] = $result->id_cluster;
				$cluster_values['id_query'] = $result->id_query;
				// include terms_meta for semantic clusters
				$cluster_values['terms_meta'] = json_decode( $result->terms_meta, TRUE );
				$clusters[] = $cluster_values;
			}
			return json_encode( $clusters );
		} else {
			return $results;
		}
	}
	
	function delete( $id_cluster )
	{
		$this->db->where( 'id_cluster', $id_cluster );
		$this->db->delete( 'cluster' );
	}
}

/* end of query_model */