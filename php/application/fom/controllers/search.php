<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Search: search entry point for web frontend
* 
* @author TMA
* @version 1.0
*/
class Search extends CI_Controller
{

	function __construct()
	{
		parent::__construct();
	}

	function index()
	{
		$this->load->library('twitter');
		$data = $this->twitter->search('trends');
		$this->load->view('search_view', $data);
	}

	// TODO: fix results
	function query_post()
	{
		$terms = $this->input->post('terms');
		$since = $this->input->post('since');
		$until = $this->input->post('until');
		$where = $this->input->post('where');
		$granularity = $this->input->post('granularity');
		$source = $this->input->post('source');

		$this->load->library('fom_search');
		$data['results'] = $this->fom_search->query( $terms, $since, $until, $where, $granularity, $source );
		$this->load->view('search_view', $data );
	}
}

/* End of file search.php */