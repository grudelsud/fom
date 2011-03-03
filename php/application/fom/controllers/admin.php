<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Admin: performs various operations
* 
* @author TMA
* @version 1.0 2011-03-02
*/
class Admin extends CI_Controller {

	function __construct()
	{
		parent::__construct();
	}
	
	function ted_update()
	{
		if( !empty($source) ) $this->db->where('src', 'ted');
		$query = $this->db->get('post');
		foreach( $query->result() as $row ) {
			$meta = json_decode( $row->meta );
			var_dump( $meta );
		}
	}
}

/* end of admin.php */