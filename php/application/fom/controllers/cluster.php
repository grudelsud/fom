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
	
	function index()
	{
		redirect('/main');
	}
	
	function read( $id_query, $format = 'json' )
	{
		$this->load->model('Cluster_model');
		if( $format == 'json' ) {
			echo $this->Cluster_model->read( $id_query, $format );
		} else {
			redirect('/main');
		}
	}

	function read_semantic( $id_parent, $format = 'json' )
	{
		$this->load->model('Cluster_model');
		if( $format == 'json' ) {
			echo $this->Cluster_model->read_semantic( $id_parent, $format );
		} else {
			redirect('/main');
		}
	}
	
	function delete( $id_cluster, $format = 'json' )
	{
//		$this->load->model('Cluster_model');
//		$this->Cluster_model->delete( $id_cluster );
		if( $format == 'json' ) {
			echo json_encode( array('delete' => $id_cluster ) );
		} else {
			redirect('/main');
		}
	}
}

/* end of file main.php */