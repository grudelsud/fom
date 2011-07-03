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
		return $this->db->insert_id();
	}

	function read_id( $id_post, $format = 'json' )
	{
		$this->db->where( 'id_post', $id_post );
		$query = $this->db->get('post');
		if( $query->num_rows() > 0 ) {
			$post = $query->row();
			if( $format == 'json' ) {

				// decode metas and unset property from original object
				$post_meta = json_decode( $post->meta );
				unset( $post->meta );

				// fetch related links
				$this->db->where( 'id_post', $id_post );
				$query_link = $this->db->get('postlink');

				if( $query_link->num_rows() > 0 ) {
					$links = '';
					foreach( $query_link->result() as $row ) {
						$links .= $row->id_link .' ';
					}
					// export links in a new property as string of concatenated ids
					$post->links = $links;
				}

				// merge objects and encode
				return json_encode( array_merge((array)$post, (array)$post_meta));
			} else {
				return $query->row();
			}
		} else {
			return json_encode( new stdClass() );
		}
	}

	function read( $lat = '', $lon = '', $geo_granularity = '', $t_start = '', $t_end = '', $t_granularity = '', $source = '' )
	{
		// TODO: implement lat/lon searches
		if( !empty($source) ) $this->db->where('src', $source);
		if( !empty($t_start) ) $this->db->where('created >', $t_start.' GMT');
		if( !empty($t_end) ) $this->db->where('created <,', $t_end.' GMT');
		$query = $this->db->get('post');
		return $query->result();
	}

	function delete( $id )
	{
		$this->db->where('id_post', $id);
		$this->db->delete('post');
		return 'OK';
	}
}

/* end of post_model */