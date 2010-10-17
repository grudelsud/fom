<?php
/**
* Class: User_model
*/
class User_model extends Model
{
	function User_model()
	{
		__construct();
	}
	function __construct()
	{
		parent::Model();
	}

	function get_data()
	{
		$query = $this->db->get('user');

		if( $query->num_rows() > 0 ) {
			return $query->result();
		} else {
			return array();
		}
	}
	
	function create_user( $data )
	{
		$this->db->insert('user', $data);
	}
}

?>