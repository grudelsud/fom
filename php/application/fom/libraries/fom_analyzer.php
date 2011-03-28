<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

require_once 'class.saplo4php.php';

/**
 * Class Fom_analyzer
 *
 * access semantic / indexing functions such as Saplo or local installation of Solr
 *
 * @author alisi
 * @version 1.0 2011-03-03
 *
 */
class Fom_analyzer {
	private $yahoo_id = 'xLrgKbPV34HLJfZhNAASAxhYPSrqtEn1CMpRn.wDvdAvRL3PQ4qewGZHglmH37W2osc-';
	private $saplo_vars = array( 'api_key' => '10f79d243d01f5d24810c63e5c6df67a', 'secret_key' => '8f9738bfd34d62b0e710a5fa9cd12fba');
	private $saplo_obj;
	private $saplo_corpus_id;

	function __construct()
	{
	}

	function yahoo_extract( $context ) {
		$url = 'http://search.yahooapis.com/ContentAnalysisService/V1/termExtraction';
		$post = 'appid='.$this->yahoo_id.'&output=json&context='.urlencode($context);
		
		$ch = curl_init();

		// set url
		curl_setopt($ch, CURLOPT_URL, $url);

		curl_setopt($ch, CURLOPT_POST,3);
		curl_setopt($ch, CURLOPT_POSTFIELDS, $post);

		//return the transfer as a string
		curl_setopt($ch, CURLOPT_HEADER, FALSE);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);

		$result = curl_exec($ch);
		return $result;
	}

	function ted_fetch_transcript( $url ) {
		$ch = curl_init();

		// set url
		curl_setopt($ch, CURLOPT_URL, $url);

		//return the transfer as a string
		curl_setopt($ch, CURLOPT_HEADER, FALSE);
		curl_setopt($ch, CURLOPT_RETURNTRANSFER, TRUE);

		$page = curl_exec($ch);

		$doc = new DOMDocument();
		@$doc->loadHTML( $page );

		$xpath = new DOMXpath( $doc );
		$elements = $xpath->query("//meta");

		$transcript = '';

		if (!is_null($elements)) {
			foreach ($elements as $element) {
				$link = $element->getAttribute('content');
				$link = substr($link, strrpos($link, 'http'));

				curl_setopt($ch, CURLOPT_URL, $link);
				$page = curl_exec($ch);

				$doc = new DOMDocument();
				@$doc->loadHTML( $page );

				$xpath = new DOMXpath( $doc );
				$transcripts = @$xpath->query("//*[@id='transcriptText']//p");

				if (!is_null($transcripts)) {
					foreach ($transcripts as $fragment) {
						$transcript .= $doc->saveXML($fragment);
					}
				}

			}
		}

		// close curl resource to free up system resources
		curl_close($ch);
		return strip_tags( $transcript );
	}

	private function init_saplosession()
	{
		try {
			$this->saplo_obj = new SaploAPI( $this->saplo_vars );
			$corpora = $this->saplo_obj->corpus_getPermissions();
				
			// taking the first corpus (0) as default, there are 2 corpora saved on this account
			$this->saplo_corpus_id = $corpora[0]['corpusId'];
		} catch(SaploException $e) {
			$message = 'SaploException: '.$e->getMessage().' (ErrorCode: '.$e->getCode().') API-JSONRequest'.$e->getJSONRequest();
			throw new Exception( $message );
		}
	}

	function corpus_get_permissions()
	{
		if( !isset($this->saplo_obj) ) {
			$this->init_saplosession();
		}
		return $this->saplo_obj->corpus_getPermissions();
	}

	function create_corpus($corpusName, $corpusInfo, $lang)
	{
		if( !isset($this->saplo_obj) ) {
			$this->init_saplosession();
		}
		$this->saplo_corpus_id = $this->saplo_obj->corpus_createCorpus($corpusName, $corpusInfo, $lang);
		return $this->saplo_corpus_id;
	}

	function list_contexts()
	{
		if( !isset($this->saplo_obj) ) {
			$this->init_saplosession();
		}
		return $this->saplo_obj->context_listContexts();
	}

	function add_article($headline, $body, $publishUrl, $speaker, $lang)
	{
		if( !isset($this->saplo_obj) ) {
			$this->init_saplosession();
		}

		$id = $this->saplo_obj->corpus_addArticle($this->saplo_corpus_id, $headline, '', $body, '', $publishUrl, $speaker, $lang);
		return $id;
	}

	function get_entitytags($articleId)
	{
		if( !isset($this->saplo_obj) ) {
			$this->init_saplosession();
		}
		try {
			return $this->saplo_obj->tags_getEntityTags($this->saplo_corpus_id, $articleId);
		} catch(SaploException $e) {
			return $e->getMessage();
		}
	}

	function kill_saplosession()
	{
		if( !isset($this->saplo_obj) ) {
			$this->init_saplosession();
		}
		$response = $this->saplo_obj->killSession();
		unset( $this->saplo_obj );
		unset( $this->saplo_corpus_id );
		return $response;
	}
}