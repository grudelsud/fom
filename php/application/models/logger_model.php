<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Logger_model
*
* @author TMA
* @version 1.0 2010-11-20
*/
class Logger_model extends CI_Model
{
	
	function __construct()
	{
		parent::__construct();
	}
	
	function create( $id_user = '', $action = '', $meta = '' )
	{
		$data = array(
			'id_user' => (empty($id_user) ? 1 : $id_user),
			'action' => $action,
			'meta' => $meta,
			'created' => date( 'Y-m-d h:i:s' )
		);
		$this->db->insert('log', $data);
	}
}

/* end of logger_model.php */