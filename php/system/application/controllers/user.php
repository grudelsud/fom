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
		$data['result'] = $this->user_model->get_user();
		$this->load->view('user_view', $data);
	}
	
	function insert()
	{
		$this->load->model('user_model');
		$this->user_model->create_user( $_POST );
		redirect('index');
	}
	
	function profile( $id = "" ) 
	{
		if( !empty( $id) ) {
			$this->load->model('user_model');
			$data['result'] = $this->user_model->get_user( $id );
			$this->load->view('profile_view', $data);
		} else {
			redirect( 'user/index' );
		}
	}
}

?>