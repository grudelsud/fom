<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Main: main entry point
* 
* @author TMA
* @version 1.0
*/
class Main extends CI_Controller
{
	
	function __construct()
	{
		parent::__construct();
		log_message('error', '[main.__construct] is loading');
		// $this->output->enable_profiler(TRUE);
	}
	
	function index()
	{
		$this->load->view('main_view');
	}
}
