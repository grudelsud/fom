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
		$data['view'] = 'includes/search';
		$this->load->view('template_view', $data);
	}

	function result()
	{
		$this->load->model('query_model');
		$data['queries'] = $this->query_model->read();
		$data['view'] = 'includes/result';
		$this->load->view('template_view', $data);
	}
	
	// example function call: http://vampireweekend.local/fom/index.php/search/dzstat/since=/until=/timespan=daily/swLat=44.09176715496926/swLon=10.120849609375/neLat=45.0310656020628/neLon=11.966552734375
	
	function dzstat( $since, $until, $timespan, $swLat, $swLon, $neLat, $neLon )
	{
		$stat_output = array();
		$since = preg_replace('/since=/', '', $since);
		$until = preg_replace('/until=/', '', $until);
		$timespan = preg_replace('/timespan=/', '', $timespan);
		$swLat = preg_replace('/swLat=/', '', $swLat);
		$swLon = preg_replace('/swLon=/', '', $swLon);
		$neLat = preg_replace('/neLat=/', '', $neLat);
		$neLon = preg_replace('/neLon=/', '', $neLon);
		
		$sql = '';
		$result = array();
		if( !empty( $since ) ) {
			$f_since = date('Y-m-d H:i:s', strtotime($since));
			$f_until = date('Y-m-d H:i:s', strtotime($since) + 3600 * 24);
			$sql = "SELECT lang, count(*) as number FROM `fom_post` WHERE lat > ".$swLat." and lat < ".$neLat." and lon > ".$swLon." and lon < ".$neLon." and created between '".$f_since."' and '".$f_until."' group by lang";
			
			$query = $this->db->query( $sql );
			$result['other'] = 0;
			$total = 0;
			$eval_langs = array('dummy', 'english', 'italian', 'french', 'spanish', 'german', 'portuguese');
			foreach( $query->result() as $row ) {
				$total += $row->number;
				if( array_search( $row->lang, $eval_langs ) ) {
					$result[$row->lang] = $row->number;
				} else {
					$result['other'] += $row->number;
				}
			}
			$result['total'] = $total;
		}
		$stat_output['result'] = $result;
//		$stat_output['query'] = $sql;
		echo json_encode( $stat_output );
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