<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Result: show clusters/queries stored on the database and perform some basics operations
* 
* @author TMA
* @version 1.0 2010-11-18
*/
class Result extends CI_Controller
{
	
	function __construct()
	{
		parent::__construct();
		// $this->output->enable_profiler(TRUE);
	}
	
	function index()
	{
		$this->load->model('query_model');
		$data['queries'] = $this->query_model->read();
		$this->load->view('result_view', $data);
	}
}

/* end of file result.php */