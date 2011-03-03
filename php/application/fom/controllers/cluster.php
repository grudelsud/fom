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
		echo json_encode(array());
	}
	
	function delete( $id_cluster )
	{
//		$this->load->model('Cluster_model');
//		$this->Cluster_model->delete( $id_cluster );
		echo json_encode( array('delete' => $id_cluster ) );
	}
}

/* end of file cluster.php */