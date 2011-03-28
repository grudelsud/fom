<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Query_model
*
* @author TMA
* @version 1.0 2010-11-20
*/
class Query_model extends CI_Model
{
	
	function __construct()
	{
		parent::__construct();
	}

	function create( $id_user = '', $query = '', $t_start = '', $t_end = '', $t_granularity = '', $lat = '', $lon = '', $geo_granularity = '', $timezone = '' )
	{
		$data = array(
			'id_user' => (empty($id_user) ? 1 : $id_user),
			'query' => $query,
			't_start' => $t_start,
			't_end' => $t_end,
			't_granularity' => $t_granularity,
			'lat' => $lat,
			'lon' => $lon,
			'geo_granularity' => $geo_granularity,
			'created' => date('Y-m-d H:i:s'),
			'timezone' => (empty($timezone) ? 0 : $timezone)
		);
		$this->db->insert('query', $data);
		return $this->db->insert_id();
	}
	
	function read()
	{
		$this->db->order_by('t_start', 'desc');
		$query = $this->db->get('query');
		return $query->result();
	}
	
	function delete( $id_query )
	{
		$this->db->where('id_query', $id_query );
		$this->db->delete('cluster');

		$this->db->where('id_query', $id_query );
		$this->db->delete('query');
	}
}

/* end of query_model */