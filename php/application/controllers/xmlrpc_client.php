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
	
	function index()
	{
		$data['result'] = 'Set params and submit this form to call XML-RPC server';
		$this->load->view('admin/xmlrpc_client_view', $data);
	}

	function post_params()
	{
		$function = $this->input->post('function');
		$server = $this->input->post('server');

		$param1 = $this->input->post('param1');
		$param2 = $this->input->post('param2');
		$param3 = $this->input->post('param3');
		$param4 = $this->input->post('param4');
		$param5 = $this->input->post('param5');
		$param6 = $this->input->post('param6');
		
		$request = array();

		// $this->xmlrpc->set_debug(TRUE);

		if( FALSE !== $function && !empty( $function ) ) $this->xmlrpc->method($function, 80);
		if( FALSE !== $server && !empty( $server ) ) $this->xmlrpc->server($server, 80);
		
		if( FALSE !== $param1 && !empty( $param1 ) ) $request[] = $param1;
		if( FALSE !== $param2 && !empty( $param2 ) ) $request[] = $param2;
		if( FALSE !== $param3 && !empty( $param3 ) ) $request[] = $param3;
		if( FALSE !== $param4 && !empty( $param4 ) ) $request[] = $param4;
		if( FALSE !== $param5 && !empty( $param5 ) ) $request[] = $param5;
		if( FALSE !== $param6 && !empty( $param6 ) ) $request[] = $param6;

		$this->xmlrpc->request($request);
		$this->xmlrpc->send_request();

		$data['result'] = '';
		$data['result']  = "Status: ".$this->xmlrpc->display_error()."\n";
		$data['result'] .= "Response:\n".var_export($this->xmlrpc->display_response(), TRUE)."\n";		
		$this->load->view('admin/xmlrpc_client_view', $data);
	}
}

/* End of xmlrpc_client.php */