<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Post_model
*
* @author TMA
* @version 1.0 2010-11-20
*/
class Post_model extends CI_Model
{
	
	function __construct()
	{
		parent::__construct();
	}
	
	function create( $content = '', $source = '', $datetime = '', $timezone = '', $id_user = '', $lat = '', $lon = '', $tw_statusid = '', $tw_replyid = '' )
	{
		$data = array(
			'id_user' => (empty($id_user) ? 1 : $id_user),
			'lat' => $lat,
			'lon' => $lon,
			'content' => $content,
			'created' => $datetime.' GMT',
			'modified' => date('Y-m-d H:i:s'),
			'timezone' => (empty($timezone) ? 0 : $timezone),
			'meta' => (empty($tw_replyid) ? '' : json_encode( array( 'in_reply_to_status_id' => $tw_replyid ) )),
			'src' => (empty($source) ? 'twitter' : $source),
			'src_id' => $tw_statusid
		);
		$this->db->insert('post', $data);
	}

	function read( $lat = '', $lon = '', $geo_granularity = '', $t_start = '', $t_end = '', $t_granularity = '' )
	{
		// TODO: implement lat/lon searches
		if( !empty($t_start) ) $this->db->where('created >', $t_start.' GMT');
		if( !empty($t_end) ) $this->db->where('created <,', $t_end.' GMT');
		$query = $this->db->get('post');
		return $query->result();
	}

	function delete( $id )
	{
		$this->db->where('id_post', $id);
		$this->db->delete('post');
	}
}

/* end of post_model */