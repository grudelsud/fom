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

	function stat( $id_query ) {
		$this->load->model('Cluster_model');
		
		$stat = array();
		$geo_clusters = $this->Cluster_model->read( $id_query, 'object' );
		foreach( $geo_clusters as $geo_cluster ) {
			$sem_clusters = $this->Cluster_model->read_semantic( $geo_cluster->id_cluster, 'object' );
			foreach( $sem_clusters as $sem_cluster ) {
				$terms = explode(';', preg_replace('/\"/', '', $sem_cluster->terms_meta));
				$stat = array_merge( $stat, $terms );
			}
		}
		$stat = array_count_values( $stat );
		arsort( $stat );
		echo json_encode( $stat );
	}

	function read_semantic( $id_parent )
	{
		$this->load->model('Cluster_model');
		echo $this->Cluster_model->read_semantic( $id_parent, 'json' );
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