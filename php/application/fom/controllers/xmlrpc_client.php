<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Xml: serves as XML-RPC entry point
* 
* @author TMA
*/
class Xmlrpc_client extends CI_Controller
{
	
	function __construct()
	{
		parent::__construct();

		$this->load->library('xmlrpc');
	}
	
	function index( $max_params = '' )
	{
		if( empty( $max_params ) || !is_numeric( $max_params ) ) {
			$data['max_params'] = 12;
		} else {
			$data['max_params'] = $max_params;
		}
		$data['result'] = 'Set params and submit this form to call XML-RPC server';
		$this->load->view('admin/xmlrpc_client_view', $data);
	}

	function post_params()
	{
//		$this->xmlrpc->set_debug(TRUE);
		$function = $this->input->post('function');
		$server = $this->input->post('server');
		$max_params = $this->input->post('max_params');

		$request = array();

		if( FALSE !== $function && !empty( $function ) ) $this->xmlrpc->method($function, 80);
		if( FALSE !== $server && !empty( $server ) ) $this->xmlrpc->server($server, 80);
		
		for( $i = 0; $i < $max_params; $i++ ) {
			$param_field = "param".($i + 1);
			$param = $this->input->post( $param_field );

			if( FALSE !== $param && !empty( $param ) ) {
				$request[] = $param;
			}
		}
		
		$this->xmlrpc->request($request);
		$this->xmlrpc->send_request();

		$data['result'] = "";
		$data['result'] .= "Status: ".$this->xmlrpc->display_error()."\n";
		$data['result'] .= "Response:\n".var_export($this->xmlrpc->display_response(), TRUE)."\n";		
		$this->load->view('admin/xmlrpc_client_view', $data);
	}
}

/* End of xmlrpc_client.php */