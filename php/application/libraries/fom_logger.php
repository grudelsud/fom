<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Fom_logger: logs actions to database
*
* @author TMA
* @version 1.0 2010-11-20
*/
class Fom_logger
{
	function log( $id_user = '', $action = '', $meta = '' ) 
	{
		$_CI =& get_instance();
		$_CI->load->model('Logger_model');
		$_CI->Logger_model->create( $id_user, $action, $meta );
	}
}

/* end of fom_logger.php */