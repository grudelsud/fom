<?php  if ( ! defined('BASEPATH')) exit('No direct script access allowed');

if ( ! function_exists('assets_url'))
{
	function assets_url($uri = '')
	{
		$CI =& get_instance();
		return $CI->config->slash_item('base_url').APPPATH.(empty($uri) ? '' : $uri);
	}
}

if ( ! function_exists('googlechart_extencode'))
{
	function googlechart_extencode($array)
	{
		$characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-.';

		$scale = 4095;
		$max = max($array);
		$scaled_array = array();
		foreach($array as $value) {
			$scaled = ($value/$max) * $scale;
			array_push($scaled_array, $scaled);
		}
		$array = $scaled_array;

		// Encode values in array.
		$encoding = 'e:';
		foreach($array as $value) {
			$first = floor($value / 64);
			$second = $value % 64;
			$encoding .= $characters[$first] . $characters[$second];
		}
		return $encoding;
	}
}
/* end of assets.php */