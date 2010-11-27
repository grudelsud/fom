<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Query: controls operations on queries
* 
* @author TMA
* @version 1.0 2010-11-25
*/
class Query extends CI_Controller
{
	
	function __construct()
	{
		parent::__construct();
	}
	
	function index()
	{
		redirect('/main');
	}
	
	function delete( $id_query )
	{
		$this->load->model('Query_model');
		$this->Query_model->delete( $id_query );
		redirect('/main');
	}
}

/* end of file main.php */