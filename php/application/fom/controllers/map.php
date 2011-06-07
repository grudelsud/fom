<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Map: show clusters/queries stored on the database, hopefully on a beautiful map
* 
* @author TMA
* @version 1.0 2010-11-18
*/
class Map extends CI_Controller
{
	
	function __construct()
	{
		parent::__construct();
		// $this->output->enable_profiler(TRUE);
	}
	
	function index()
	{
		if( !$this->session->userdata('id_user') ) {
			$this->load->view('auth_view');
		} else {		
			$this->load->model('query_model');
			$data['queries'] = $this->query_model->read();
			$this->load->view('map_view', $data);
		}
	}
}

/* end of file map.php */