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
	
	function query()
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
		
		if( FALSE !== $terms && ! empty( $terms ) ) $query['q'] = $terms;
		if( FALSE !== $since && ! empty( $since ) ) $query['since'] = $since;
		if( FALSE !== $until && ! empty( $until ) ) $query['until'] = $until;

		// if( FALSE !== $where && ! empty( $where ) ) $query['near'] = str_replace( ' ', '+', $where );
		// $query['geocode'] = "37.781157,-122.398720,10mi";
		// http://search.twitter.com/search?q=&ands=bagel+bakery&near=islington&since=2010-11-01&until=2010-11-14

		$this->load->library('twitter');
		$results = $this->twitter->search('search', $query);

		log_message('error', '[search] twitter query exec - q:'.$query['q'].' s:'.$query['since'].' u:'.$query['until'] );
		$this->load->view('search_result', $results );
	}
}

/* End of file search.php */