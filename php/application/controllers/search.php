<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Search: main entry point
* 
* @author TMA
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

	// TODO: bring back to xmlrpc.php after proper implementation of search library
	function xmlrpc()
	{
		$this->load->library('xmlrpc');
		$this->load->library('xmlrpcs');

		$config['functions']['query'] = array('function' => 'Search.query_xmlrpc');
		$config['object'] = $this;

		log_message('error', '[xml.index] ready to serve');
		$this->xmlrpcs->initialize( $config );
		$this->xmlrpcs->serve();
	}

	// TODO: execute search methods as external library
	function query_post()
	{
		$terms = $this->input->post('terms');
		$since = $this->input->post('since');
		$until = $this->input->post('until');
		$where = $this->input->post('where');
		$granularity = $this->input->post('granularity');
		$source = $this->input->post('source');

		$query = array();

		$term_array = explode( ' ', $terms );
		$term_array = array_filter( $term_array, 'urlencode' );
		$terms = implode( '+', $term_array );

		$results = $this->_query_exec( $terms, $since, $until, $where, $granularity, $source );
		$this->load->view('search_result', $results );
	}

	// TODO: execute search methods as external library
	function query_xmlrpc( $request )
	{

		$this->load->library('xmlrpc');    
		$parameters = $request->output_parameters();

		$response = array(
		array(
			'you_said'  => $parameters['0'],
			'i_respond' => 'Not bad at all.'
			),'struct');
		log_message('error', '[search] sending xml-rpc response');
		return $this->xmlrpc->send_response($response);
	}

	// TODO: move to proper class in a library
	private function _query_exec( $terms, $since = "", $until = "", $where = "", $granularity = "", $source = "" )
	{	
		if( FALSE !== $terms && ! empty( $terms ) ) $query['q'] = $terms;
		if( FALSE !== $since && ! empty( $since ) ) $query['since'] = $since;
		if( FALSE !== $until && ! empty( $until ) ) $query['until'] = $until;

		// TODO: based on where and granularity, search coords on google and add a query param as below
		// $query['geocode'] = "37.781157,-122.398720,10mi";

		log_message('error', '[search] twitter query exec - q:'.$query['q'].' s:'.$query['since'].' u:'.$query['until'] );

		$this->load->library('twitter');
		return $this->twitter->search('search', $query);
	}
}

/* End of file search.php */