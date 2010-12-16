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
	
	function __construct()
	{
	}
	
	function query( $terms, $since = "", $until = "", $where = "", $granularity = "", $source = "" )
	{
		$cmd = 'java -jar ../java/fom/fom.jar '.
			'--twitter --query "'.$terms.'" ' .
			'--since '.$since.' --until '.$until.' --nearLat 51.535 --nearLon -0.104 ' .
			'--radius 10 --geoGran neighborhood --rpcLog';

		return exec( $cmd );
	}
}

?>