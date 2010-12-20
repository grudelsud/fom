<?php

/**
* Class Fom_search: the name of the game
*
* @author TMA
* @version 1.0 2010-11-19
*/
require_once('Twitter.php');

class Fom_search
{
	/**
	 * valid for domain londondroids.com
	 * @var string
	 */
	private static $gmaps_key = 'ABQIAAAAqwLZgY0MgTpdTzFRkv7XSxQR8fRgaKvmhRClFalgEB-1-irkuxR4b6kmn6C4LJmn2rUFvzwX3enkJA';

	function __construct()
	{
	}
	
	function query( $terms, $since = "", $until = "", $where = "", $granularity = "", $source = "" )
	{
		$lat = 666;
		$lon = 666;

		if( !empty( $where) ) {
			
			$url = 'http://api.twitter.com/1/geo/search.json?query='.urlencode( $where );
	
			$ch = curl_init();
	
			curl_setopt($ch, CURLOPT_URL, $url);
			curl_setopt($ch, CURLOPT_HEADER, FALSE);
			curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);
			
			$res_json = curl_exec($ch);
			curl_close($ch);
	
			$res_obj = json_decode( $res_json );
			
			foreach ( $res_obj->result->places as $place ) {
				$coords = $place->bounding_box->coordinates[0];
				$lat = ($coords[0][0] + $coords[1][0] + $coords[2][0] + $coords[3][0] ) / 4; 
				$lon = ($coords[0][1] + $coords[1][1] + $coords[2][1] + $coords[3][1] ) / 4; 
			}
		}
		
		if( empty( $since ) ) {
			$since = date("Y-m-d");
		}
		if( empty( $until ) ) {
			$until = date("Y-m-d");
		}
		$cmd = 'java -jar ../java/fom/fom.jar '.
			'--twitter --query "'.$terms.'" ' .
			'--since '.$since.' --until '.$until.
			($lat != 666 ? ' --nearLat '.$lat.' --nearLon '.$lon.' --radius 10 ' : ' ').
			'--geoGran neighborhood --rpcLog';

		return exec( $cmd );
	}
}

?>