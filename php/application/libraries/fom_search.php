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
		$term_array = explode( ' ', $terms );
		$term_array = array_filter( $term_array, 'urlencode' );
		$terms = implode( '+', $term_array );

		$query['q'] = $terms;
		
		$query['since'] = $since;
		$query['until'] = $until;

		// TODO: based on where and granularity, search coords on google and add a query param as below
		// $query['geocode'] = "37.781157,-122.398720,10mi";

		$twitter = new Twitter();
		$results = $twitter->search('search', $query);

		return $results;
	}
}

?>