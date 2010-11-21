<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');

if ( ! function_exists('assets_url'))
{
	function assets_url($uri = '')
	{
		$CI =& get_instance();
		return $CI->config->slash_item('base_url').APPPATH.(empty($uri) ? '' : $uri);
	}
}

/* end of assets.php */