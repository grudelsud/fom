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
//		$this->load->library('twitter');
//		$data['trends'] = $this->twitter->search('trends');
		$data['view'] = 'search';
		$this->load->view('template_view', $data);
	}

	function result()
	{
		$this->load->model('query_model');
		$data['queries'] = $this->query_model->read();
		$data['view'] = 'result';
		$this->load->view('template_view', $data);
	}
	
	function query()
	{
		$terms = $this->input->post('terms');
		$since = $this->input->post('since');
		$until = $this->input->post('until');
		$where = $this->input->post('where');
		$granularity = $this->input->post('granularity');
		$source = $this->input->post('source');

//		$this->load->library('fom_search');
//		$data['results'] = $this->fom_search->query( $terms, $since, $until, $where, $granularity, $source );
		$this->result();
	}
}

/* End of file search.php */