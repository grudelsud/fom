<?php
/**
* Class: Post
*/
class Post extends Controller
{
	function Post()
	{
		__construct();
	}

	function __construct()
	{
		parent::Controller();
	}
	
	function index()
	{
		$this->load->model('post_model');
		$data['result'] = $this->post_model->get_data();
		$this->load->view('post_view', $data);
	}
}

?>