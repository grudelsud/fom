<?php
/**
* Class User
*/
class User extends Controller
{
	function User()
	{
		__construct();
	}
	function __construct()
	{
		parent::Controller();
		$this->load->helper('url');
		$this->load->helper('form');
	}

	function index()
	{
		$this->load->model('user_model');
		$data['result'] = $this->user_model->get_data();
		$this->load->view('user_view', $data);
	}
	
	function insert()
	{
		$this->load->model('user_model');
		$this->user_model->create_user( $_POST );
		$this->load->view('user_view', $data);
	}
}

?>