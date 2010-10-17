<?php
/**
* Class: Post_model
*/
class Post_model extends Model
{
	function Post_model()
	{
		__construct();
	}

	function __construct()
	{
		parent::Model();
	}
	
	function get_data()
	{
		$query = $this->db->get('post');

		if( $query->num_rows() > 0 ) {
			return $query->result();
		} else {
			return array();
		}
	}
}

?>