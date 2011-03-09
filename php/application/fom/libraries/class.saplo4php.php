<?php

/**
 * EXAMPLE USE
 * 
 * try {
 *	
 *	$c = new SaploAPI(array('api_key' => 'API_KEY', 'secret_key' => 'SECRET_KEY'));
 *	
 * } catch(SaploException $e) {
 *	
 *	echo 'SaploException: '.$e->getMessage().' (ErrorCode: '.$e->getCode().') API-JSONRequest'.$e->getJSONRequest();
 * 
 * }
 * 
 * 
 * 
 */


class SaploAPI {

        private $url = 'http://api.saplo.com/rpc/json';
        private $session;
        private $jsonRequest;
        
        
		/**
		 * @param String $apiKey
		 * @param String $secretKey
		 * @throws SaploException
		 */
		public function __construct($keys) {
        	$session = $this->createSession($keys['api_key'], $keys['secret_key']);
            $this->setSession($session);  
        }

        /*
         * CORPUS METHODS
         */
        
        /**
         * Create new corpus to work on
         * @param String $corpusName
         * @param String $corpusInfo
         * @param String $lang
         * @param String $asyncId
         * @return Integer corpus id that was created.
         * @throws SaploException
         */
        public function corpus_createCorpus($corpusName, $corpusInfo, $lang, $asyncId = 0) {
                $params = array($corpusName, $corpusInfo, $lang);
                $response = $this->doRequest("corpus.createCorpus",$params, $asyncId);
                $result = $this->parseResponse($response);
        		return $result['corpusId'];
        }

        
        /**
         * Get all corpus permissions that your user have.
         * 1 = write, 2 = read
         * @param String $asyncId Id for the JSON-RPC call if you want to make async calls.
         * 					      Can be either int or string.
         * @return Array
         * @throws SaploException
         */
        public function corpus_getPermissions($asyncId = 0) {
                $params = array();
                $response = $this->doRequest("corpus.getPermissions",$params, $asyncId);
                return $this->parseResponse($response);
        }
        
        public function corpus_getInfo($corpusId, $asyncId = 0) {
                $params = array($corpusId);
                $response = $this->doRequest("corpus.getInfo",$params, $asyncId);
                return $this->parseResponse($response);
        }
        
		/**
		 * Add an article to Saplo API.
		 * @param Integer $corpusId
		 * @param String $headline
		 * @param String $lead
		 * @param String $body
		 * @param String $publishStart
		 * @param String $publishUrl
		 * @param String $authors
		 * @param String $lang
		 * @param String $asyncId Id for the JSON-RPC call if you want to make async calls.
         * 					      Can be either int or string.
		 * @return Integer article id the article got in that corpus
		 * @throws SaploException
		 */
		public function corpus_addArticle($corpusId, $headline, $lead, $body, $publishStart, $publishUrl, $authors, $lang, $asyncId = 0) {
                $params = array((int)$corpusId, $headline, $lead, $body, $publishStart, $publishUrl, $authors, $lang);
                $response = $this->doRequest("corpus.addArticle",$params, $asyncId);
                $result = $this->parseResponse($response);
                return $result['articleId'];
        }

        /**
         * Get Article as an array with headline and publishUrl
         * @param Integer $corpusId
         * @param Integer $articleId
         * @param String $asyncId Id for the JSON-RPC call if you want to make async calls.
         * 					      Can be either int or string.
         * @return Array
         * @throws SaploException
         */
        public function corpus_getArticle($corpusId, $articleId, $asyncId = 0) {
                $params = array((int)$corpusId, (int)$articleId);
                $response = $this->doRequest("corpus.getArticle",$params, $asyncId);
                return $this->parseResponse($response);
        }
        
        /**
         * Update an article with new information
         * @param Integer $corpusId Corpus Id where the Article Exists
         * @param Integer $articleId Article Id for the article
         * @param String $headline
         * @param String $lead
         * @param String $body
         * @param String $publishStart (Datetime formated as YYYY-MM-DD HH:MM:SS)
         * @param String $publishUrl
         * @param String $authors
         * @param String $lang
         * @param String $asyncId Id for the JSON-RPC call if you want to make async calls.
         * 					      Can be either int or string.
         * @return Boolean
         * @throws SaploException
         */
        public function corpus_updateArticle($corpusId, $articleId, $headline, $lead, $body, $publishStart, $publishUrl, $authors, $lang, $asyncId = 0) {
                $params = array((int)$corpusId, (int)$articleId, $headline, $lead, $body, $publishStart, $publishUrl, $authors, $lang);
                $response = $this->doRequest("corpus.updateArticle",$params, $asyncId);
                return $this->parseResponse($response);
        }
        
        
        /*
         * TAGS METHODS
         */
        
        /**
         * Get All Entity Tags that exists in an article.
         * 
         * @corpusId integer - id for the corpus the article exists in
         * @articleId integer - unique id for the article in the corpus
         * @wait integer - how long the request should wait for an answer before closing
         * @param String $asyncId Id for the JSON-RPC call if you want to make async calls.
         * 					      Can be either int or string.
         */
        public function tags_getEntityTags($corpusId, $articleId, $wait = 0, $asyncId = 0) {
                $params = array((int)$corpusId, (int)$articleId, $wait);
                $response = $this->doRequest("tags.getEntityTags",$params, $asyncId);
                return $this->parseResponse($response);
        }
        
        /**
         * Get a single tag
         * @param Integer $corpusId
         * @param Integer $articleId
         * @param Integer $tagId
         * @param String $asyncId Id for the JSON-RPC call if you want to make async calls.
         * 					      Can be either int or string.
         * @return mixed
         */
        public function tags_getTag($corpusId, $articleId, $tagId, $asyncId = 0) {
                $params = array((int)$corpusId, (int)$articleId, $tagId);
                $response = $this->doRequest("tags.getTag",$params, $asyncId);
                return $this->parseResponse($response);
        }
        
        /**
         * Add a tag to the API. Gives feedback to the API which improves the technology.
         * 
         * @param Integer $corpusId
         * @param Integer $articleId
         * @param String $tagWord
         * @param Integer $tagTypeId
         * @param String $asyncId Id for the JSON-RPC call if you want to make async calls.
         * 					      Can be either int or string.
         * @return Integer tagId created
         */
        public function tags_addTag($corpusId, $articleId, $tagWord, $tagTypeId, $asyncId = 0) {
                $params = array((int)$corpusId, (int)$articleId, $tagWord, (int)$tagTypeId);
                $response = $this->doRequest("tags.addTag",$params, $asyncId);
                return $this->parseResponse($response);
        }
        
        public function tags_deleteTag($corpusId, $articleId, $tagId, $asyncId = 0) {
                $params = array((int)$corpusId, (int)$articleId, $tagId);
                $response = $this->doRequest("tags.deleteTag",$params, $asyncId);
                return $this->parseResponse($response);
        }

        public function tags_updateTag($corpusId, $articleId, $tagId, $tagWord, $tagTypeId, $asyncId = 0) {
                $params = array((int)$corpusId, (int)$articleId, (int)$tagId, $tagWord, (int)$tagTypeId);
                $response = $this->doRequest("tags.updateTag",$params, $asyncId);
                return $this->parseResponse($response);
        }
        
        /*
         * MATCH METHODS
         */

        
        /**
         * Get similar articles for a specific article.
         * @corpusId integer - id for the corpus the article exists in
         * @articleId integer - unique id for the article in the corpus
         * @wait integer - how long the request should wait for an answer before closing (default 0 seconds)
         * @numberOfResults integer - how many results that will be returned (default 10 results)
         * @threshold decimal - the threshold for how similar an article should be (default 0.75)
         * @param String $asyncId Id for the JSON-RPC call if you want to make async calls.
         * 					      Can be either int or string.
         */
        public function match_getSimilarArticles($corpusId, $articleId, $wait = 0, $numberOfResults = 10, $threshold = 0.75, $asyncId = 0) {

                $params = array((int)$corpusId, (int)$articleId, (int)$wait, (int)$numberOfResults, $threshold);
                $response = $this->doRequest("match.getSimilarArticles",$params, $asyncId);
                return $this->parseResponse($response);
        }
        

        public function match_getMatch($corpusId, $articleId, $matchId, $asyncId = 0) {
                $params = array((int)$corpusId, (int)$articleId, (int)$matchId);
                $response = $this->doRequest("match.getMatch",$params, $asyncId);
                return $this->parseResponse($response);
        }
        
        public function match_deleteMatch($corpusId, $articleId, $matchId, $asyncId = 0) {
                $params = array((int)$corpusId, (int)$articleId, (int)$matchId);
                $response = $this->doRequest("match.deleteMatch",$params, $asyncId);
                return $this->parseResponse($response);
        }
        

        
        /*
         * CONTEXT FUNCTIONS 
         */
        
        /**
         * Create a new context four your user account.
         * @$contextName string - the name you want to give to your context. This must be unique for your account.
         * @$contextDesc string - the description you want to give to your context.
         * @param String $asyncId Id for the JSON-RPC call if you want to make async calls.
         * 					      Can be either int or string.
         */  
        public function context_createContext($contextName, $contextDesc, $asyncId = 0) {
                $params = array($contextName, $contextDesc);
                $response = $this->doRequest("context.createContext",$params, $asyncId);
                $context = $this->parseResponse($response);
                return $context['contextId'];
        }
        
        /**
         * Delete an existing context. Warning this can not be undone.
         * @contextId int - the context id you want to delete.
         * @param String $asyncId Id for the JSON-RPC call if you want to make async calls.
         * 					      Can be either int or string.
         * @return boolean - true/false
         */
        public function context_deleteContext($contextId, $asyncId = 0) {
                $params = array((int)$contextId);
                $response = $this->doRequest("context.deleteContext",$params, $asyncId);
                return $this->parseResponse($response);
        }
        
        /**
         * Update an existing contexts name or/and description.
         * @param String $asyncId Id for the JSON-RPC call if you want to make async calls.
         * 					      Can be either int or string.
         */
        public function context_updateContext($contextId, $contextName, $contextDescription, $asyncId = 0) {
                $params = array((int)$contextId, $contextName, $contextDescription);
                $response = $this->doRequest("context.updateContext",$params, $asyncId);
                return $this->parseResponse($response);
        }
        
        public function context_addLikeArticles($contextId, $corpusId, $articles, $wait = 0, $asyncId = 0) {

                if(!is_array($articles)) {      
                        $articles = array($articles);
                }

                $articles = array( "javaClass" => "java.util.ArrayList",
                                                        "list" => $articles);
                                
                $params = array((int)$contextId, (int)$corpusId, $articles, (int)$wait);
                $response = $this->doRequest("context.addLikeArticles",$params, $asyncId);
                return $this->parseResponse($response);
        }
        
        
        public function context_deleteLikeArticles($contextId, $corpusId, $articles, $wait = 0, $asyncId = 0) {
                
                if(!is_array($articles)) {      
                        $articles = array($articles);
                }

                $articles = array( "javaClass" => "java.util.ArrayList",
                                                        "list" => $articles);
                
                $params = array((int)$contextId, (int)$corpusId, $articles, (int)$wait);
                $response = $this->doRequest("context.deleteLikeArticles",$params, $asyncId);
                return $this->parseResponse($response);
        }
        
        
        public function context_getContextSimilarity($corpusId, $articleId, $contextIds, $threshold = 0.0, $limit = 20, $wait = 0, $asyncId = 0) {

                if(!is_array($contextIds)) {    
                        $contextIds = array($contextIds);
                }

                $contextIds = array( "javaClass" => "java.util.ArrayList",
                                                        "list" => $contextIds);

                $params = array((int)$corpusId, (int)$articleId, $contextIds, $threshold, (int)$limit, (int)$wait);
                $response = $this->doRequest("context.getContextSimilarity",$params, $asyncId);
                return $this->parseResponse($response);
        }

        public function context_listContexts($asyncId = 0) {
                $params = array();
                $response = $this->doRequest("context.listContexts",$params, $asyncId);
                return $this->parseResponse($response);
        }
        
        public function context_listContextArticles($contextId, $asyncId = 0) {
                $params = array((int)$contextId);
                $response = $this->doRequest("context.listContextArticles",$params, $asyncId);
                return $this->parseResponse($response);
        }
        
		public function context_listContextsByArticle($corpusId, $articleId, $asyncId = 0) {
			$params = array((int)$corpusId, (int)$articleId);
			$response = $this->doRequest("context.listByArticle",$params, $asyncId);
			return $this->parseResponse($response);
		}

        /*
         * AUTH METHODS
         */
        
        public function killSession() {
                $params = array();
                $response = $this->doRequest("auth.killSession",$params);
                return $this->parseResponse($response);
        }
        
        private function createSession($apiKey, $secretKey, $asyncId = 0) {

                $params = array($apiKey, $secretKey);
                $response = $this->doRequest("auth.createSession",$params, $asyncId);

				return $result = $this->parseResponse($response);
        		
		}
        

        private function doRequest($method, $params, $id = 0) {
                $postUrl = $this->url.";jsessionid=".$this->getSession();
                $request = array( "method" => $method,
                                "params" => $params,
                                "id" => $id );
                
                $requestJson = json_encode($request);
                $this->jsonRequest = $requestJson; //Set so we can include this in exception.
                
				//Saplo_API_Client::debug($requestJson);
                
                //Get length of post
                $postlength = strlen($requestJson);

                //open connection
                $ch = curl_init();

                //set the url, number of POST vars, POST data
                curl_setopt($ch,CURLOPT_URL,$postUrl);
                curl_setopt($ch,CURLOPT_POST,$postlength);
                curl_setopt($ch,CURLOPT_POSTFIELDS,$requestJson);
                curl_setopt($ch, CURLOPT_RETURNTRANSFER,true);

                $response = curl_exec($ch);

                //close connection
                curl_close($ch);

                return $response;
        }

        private function setSession($session) {
                $this->session = $session;
        }

        private function getsession() {
                return $this->session;
        }


        static function debug($str) {
                echo "\n\n" .$str."\n";
        }
        
        private function parseResponse($response) {
				$parsed = json_decode($response, true);
                if(isset($parsed['result'])){
                	return $parsed['result'];
                }else if(isset($parsed['error'])) {
					throw new SaploException($parsed['error']['msg'], $parsed['error']['code'], $this->jsonRequest);
                };
        }
}

/**
 * Throws exception from Saplo API.
 * Saplo API will return a msg and a code which is passed forward.
 */
class SaploException extends Exception{
	
	private $jsonRequest;
	
	public function __construct($message, $code = 0, $jsonRequest = NULL, Exception $previous = null) {
//        parent::__construct($message, $code, $previous); //PHP version > 5.3.0
        parent::__construct($message, $code); //PHP version < 5.3.0
        $this->jsonRequest = $jsonRequest;
	}
	
	public function getJSONRequest() {
		return $this->jsonRequest;
	}
    
}