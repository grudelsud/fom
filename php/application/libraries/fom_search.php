<?php

/**
* Class Fom_search: the name of the game
*
* @author TMA
* @version 1.0 2010-11-19
*/
class Fom_search
{
	
	function __construct()
	{
	}
	
	function query( $terms, $since = "", $until = "", $where = "", $granularity = "", $source = "" )
	{
		$query['q'] = $terms;
		$query['since'] = $since;
		$query['until'] = $until;

		// TODO: based on where and granularity, search coords on google and add a query param as below
		// $query['geocode'] = "37.781157,-122.398720,10mi";
		// return $this->twitter->search('search', $query);

		return '[your search: '.$terms.'][result: all good]';
	}
}

?>