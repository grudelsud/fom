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

	function index()
	{
		$config['functions']['post_create'] = array('function' => 'Xmlrpc.post_create');
		$config['functions']['post_read'] = array('function' => 'Xmlrpc.post_read');
		$config['functions']['post_delete'] = array('function' => 'Xmlrpc.post_delete');

		$config['functions']['query'] = array('function' => 'Xmlrpc.query');

		$config['functions']['log'] = array('function' => 'Xmlrpc.log');

		$config['object'] = $this;

		// $this->fom_logger->log('', 'info', '[xmlrpc.index] ready to serve');
		$this->xmlrpcs->initialize( $config );
		$this->xmlrpcs->serve();
	}
	
	function post_create( $request ) 
	{
		$parameters = $request->output_parameters();

		$content     = isset($parameters['0']) ? $parameters[0] : '';
		$source      = isset($parameters['1']) ? $parameters[1] : ''; 
		$datetime    = isset($parameters['2']) ? $parameters[2] : ''; 
		$timezone    = isset($parameters['3']) ? $parameters[3] : ''; 
		$id_user     = isset($parameters['4']) ? $parameters[4] : ''; 
		$lat         = isset($parameters['5']) ? $parameters[5] : ''; 
		$lon         = isset($parameters['6']) ? $parameters[6] : ''; 
		$tw_statusid = isset($parameters['7']) ? $parameters[7] : ''; 

		$this->load->model('Post_model');
		$this->Post_model->create($content, $source, $datetime, $timezone, $id_user, $lat, $lon, $tw_statusid);

		// TODO: check return code
		$response = array(
			array('post_create' => json_encode( $parameters )),
			'struct'
		);

		$this->fom_logger->log('', 'info', '[xmlrpc.post_create]');
		return $this->xmlrpc->send_response($response);
	}

	function post_read( $request ) 
	{
		$parameters = $request->output_parameters();
		
		$lat             = isset($parameters['0']) ? $parameters[0] : ''; 
		$lon             = isset($parameters['1']) ? $parameters[1] : ''; 
		$geo_granularity = isset($parameters['2']) ? $parameters[2] : ''; 
		$t_start         = isset($parameters['3']) ? $parameters[3] : ''; 
		$t_end           = isset($parameters['4']) ? $parameters[4] : ''; 
		$t_granularity   = isset($parameters['5']) ? $parameters[5] : '';

		$this->load->model('Post_model');
		$result = $this->Post_model->read( $lat, $lon, $geo_granularity, $t_start, $t_end, $t_granularity );

		// TODO: check return code
		$response = array(
			array('post_read' => var_export($result, TRUE)),
			'struct'
		);
		
		$this->fom_logger->log('', 'info', '[xmlrpc.post_read]');
		return $this->xmlrpc->send_response($response);
	}

	function post_delete( $request ) 
	{
		$parameters = $request->output_parameters();
		$id = isset($parameters['0']) ? $parameters[0] : '';

		$this->load->model('Post_model');
		$this->Post_model->delete( $id );
		
		// TODO: check return code on delete
		$response = array(
			array('post_delete'  => json_encode( $parameters )),
			'struct'
		);
		
		$this->fom_logger->log('', 'info', '[xmlrpc.post_delete]');
		return $this->xmlrpc->send_response($response);
		
	}

	function query( $request )
	{
		$this->load->library('fom_search');
		$parameters = $request->output_parameters();
		
		$terms       = isset($parameters['0']) ? $parameters[0] : '';
		$since       = isset($parameters['1']) ? $parameters[1] : '';
		$until       = isset($parameters['2']) ? $parameters[2] : '';
		$where       = isset($parameters['3']) ? $parameters[3] : '';
		$granularity = isset($parameters['4']) ? $parameters[4] : '';
		$source      = isset($parameters['5']) ? $parameters[5] : '';
		
		$this->load->model('Query_model');
		$query = json_encode( array( 'terms' => $terms, 'since' => $since, 'until' => $until, 'where' => $where, 'granularity' => $granularity, 'source' => $source ) );
		$this->Query_model->create('', $query, $since, $until );
		
		$result = $this->fom_search->query( $terms, $since, $until, $where, $granularity, $source );

		$response = array(
			array('query'  => $query,'response' => var_export($result, true)),
			'struct'
		);
		
		$this->fom_logger->log('', 'info', '[xmlrpc.query] sending xml-rpc response');
		return $this->xmlrpc->send_response($response);
	}

	function log( $request ) 
	{
		$parameters = $request->output_parameters();

		$action = isset($parameters['0']) ? $parameters[0] : 'info'; 
		$meta   = isset($parameters['1']) ? $parameters[1] : '[xmlrpc.log]'; 

		// TODO: check return code
		$response = array(
			array('log' => json_encode( $parameters )),
			'struct'
		);

		$this->fom_logger->log('', $action, $meta);
		return $this->xmlrpc->send_response($response);
	}

}

/* End of xmlrpc.php */