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

	// example function call: http://vampireweekend.local/fom/index.php/search/dzstat/since=/until=/timespan=daily/swLat=44.09176715496926/swLon=10.120849609375/neLat=45.0310656020628/neLon=11.966552734375
	function dzstat( $since, $until, $timespan, $swLat, $swLon, $neLat, $neLon )
	{
		$stat_output = array();
		$since = strtotime( preg_replace('/p_since=/', '', $since) );
		$until = strtotime( preg_replace('/p_until=/', '', $until) );
		$timespan = preg_replace('/p_timespan=/', '', $timespan);
		$swLat = preg_replace('/swLat=/', '', $swLat);
		$swLon = preg_replace('/swLon=/', '', $swLon);
		$neLat = preg_replace('/neLat=/', '', $neLat);
		$neLon = preg_replace('/neLon=/', '', $neLon);
		
		$sql = '';
		$result = array();
		$eval_langs = array('total', 'english', 'italian', 'french', 'spanish', 'german', 'portuguese');
		
		if( $since && $until ) {
			if( $timespan == 'daily' ) {
				$t_increment = 86400;
			} else {
				$t_increment = 3600;
			}
			for( $i = $since; $i <= $until; $i += $t_increment ) {
				$f_since = date('Y-m-d H:i:s', $i);
				$f_until = date('Y-m-d H:i:s', $i + $t_increment);

				$sql = "SELECT lang, count(*) as number FROM `fom_post` WHERE lat > ".$swLat." and lat < ".$neLat." and lon > ".$swLon." and lon < ".$neLon." and created between '".$f_since."' and '".$f_until."' group by lang";
				$query = $this->db->query( $sql );

				$day_result = array_fill_keys($eval_langs, 0);
				$day_result['other'] = 0;

				foreach( $query->result() as $row ) {
					$day_result['total'] += $row->number;

					if( array_search( $row->lang, $eval_langs ) ) {
						$day_result[$row->lang] = $row->number;
					} else {
						$day_result['other'] += $row->number;
					}
				}
				if( $t_increment == 3600 ) {
					$since_hr = date('y.n.j D H.i', $i);
				} else {
					$since_hr = date('y.n.j D', $i);
				}

				$result[] = array_merge( array('date'=>$since_hr), $day_result);
			}
			
		}
		$stat_output['aaData'] = $result;
//		$stat_output['query'] = $sql;
		echo json_encode( $stat_output );
	}

	/*
	 * unused, used to be the endpoint for views/search_form.php
	 */
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