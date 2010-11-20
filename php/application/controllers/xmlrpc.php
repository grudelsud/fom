<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
* Class Xml: serves as XML-RPC entry point
* 
* @author TMA
*/
class Xmlrpc extends CI_Controller
{
	
	function __construct()
	{
		parent::__construct();

		$this->load->library('xmlrpc');
		$this->load->library('xmlrpcs');
	}

	// TODO: codeigniter bug, cannot execute function from other controllers, move search function to external library, as it should be
	function index()
	{
		$config['functions']['query'] = array('function' => 'Xmlrpc.query');
		$config['object'] = $this;

		log_message('error', '[xmlrpc.index] ready to serve');
		$this->xmlrpcs->initialize( $config );
		$this->xmlrpcs->serve();
	}
	
	function query( $request )
	{
		$this->load->library('fom_search');
		$this->load->library('xmlrpc');
		$parameters = $request->output_parameters();
		
		$terms = isset($parameters['0']) ? $parameters[0] : '';
		$result = $this->fom_search->query( $terms );

		$response = array(
		array(
			'param1'  => $terms,
			'response' => $result
			),'struct');
		log_message('error', '[xmlrpc.query] sending xml-rpc response');
		return $this->xmlrpc->send_response($response);
	}
	
}

/* End of xmlrpc.php */