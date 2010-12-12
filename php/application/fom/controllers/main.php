<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Main: main entry point, used to redirect where necessary
* 
* @author TMA
* @version 1.0 2010-11-18
*/
class Main extends CI_Controller
{
	
	function __construct()
	{
		parent::__construct();
		// $this->output->enable_profiler(TRUE);
	}
	
	function index()
	{
		redirect('/result');
	}
}

/* end of file main.php */