<?php if ( ! defined('BASEPATH')) exit('No direct script access allowed');

/**
 * Class Admin: performs various operations
 *
 * @author TMA
 * @version 1.0 2011-03-02
 */
class Admin extends CI_Controller {

	function __construct()
	{
		parent::__construct();
	}


	function index()
	{
		log_message('error', '--- we are in admin now ---');
		$data['view'] = 'admin/admin';
		$this->load->view('template_view', $data);

	}

	/**
	 *
		1. replace all double quotes with corresponding escape sequence (\" in mysql)
		2. format column "published" with custom field yyyy-mm-dd
		3. create an empty column on the left
		4. paste in the first row the following code (identifying the sequence: content;created;modified;timezone;meta;src)

		=""&G2&";"&text(J2, "yyyy-mm-dd")&";"&text(J2, "yyyy-mm-dd")&";0;{\""url\"":\"""&D2&"\"", \""speaker\"":\"""&E2&"\"", \""title\"":\"""&F2&"\"", \""event\"":\"""&H2&"\"", \""duration\"":\"""&I2&"\""};ted"

	 *
	 */
	function ted_read_events()
	{
		$this->db->where('src', 'ted');
		$query = $this->db->get('post');

		$events = array();

		// read event meta and create events array
		foreach( $query->result() as $row ) {
			$meta = json_decode( $row->meta );

			if( isset( $meta->event ) ) {
				$event = $meta->event;
			} else {
				$event = 'undefined';
			}
			if( isset( $meta->speaker ) ) {
				$speaker = $meta->speaker;
			} else {
				$speaker = 'undefined';
			}
			if( isset( $meta->title ) ) {
				$title = $meta->title;
			} else {
				$title = 'undefined';
			}
			if( isset( $meta->url ) ) {
				$url = $meta->url;
			} else {
				$url = 'undefined';
			}
			$events[$event][$row->id_post]['content'] = $row->content;
			$events[$event][$row->id_post]['speaker'] = $speaker;
			$events[$event][$row->id_post]['title'] = $title;
			$events[$event][$row->id_post]['url'] = $url;
		}
//		var_dump($events);
		return $events;
	}

	function ted_fetch_transcripts()
	{
		$events = $this->ted_read_events();
		$this->load->library('fom_analyzer');

		foreach( $events as $name => $event ) {
			if( $name != 'undefined') {
				foreach( $event as $id_post => $talk ) {

					$transcript = $this->fom_analyzer->ted_fetch_transcript( $talk['url'] );

					$link_data = array('uri'=>$talk['url'], 'text'=>$transcript);
					$this->db->insert('link',$link_data);

					$id_link = $this->db->insert_id();

					$postlink_data = array('id_post'=>$id_post, 'id_link'=>$id_link);
					$this->db->insert('postlink', $postlink_data);
				}
			}
		}
	}

	function ted_yahoo_terms()
	{
		$this->load->library('fom_analyzer');

		$time = date('Y-m-d h:i:s');
		
		// save yahoo terms as if it was a clustering query
		$query_data = array('id_user'=>1,'query'=>'yahoo extract','created'=>$time,'t_start'=>$time,'t_end'=>$time,'t_granularity'=>'day');
		$this->db->insert('query',$query_data);
		$id_query = $this->db->insert_id();

		// load ted's vocabulary
		$this->db->where('name','ted');
		$query_vocab = $this->db->get('vocabulary');
		
		if( $query_vocab->num_rows() > 0 ) {
			$row = $query_vocab->row();
			$id_vocabulary = $row->id_vocabulary;
		}

		// load all posts where src == ted
		$this->db->where('src','ted');
		$query_post = $this->db->get('post');

		foreach ( $query_post->result() as $row ) {
			
			// fetch terms from yahoo
			$terms_json = $this->fom_analyzer->yahoo_extract( $row->content );
			$terms = json_decode( $terms_json );

			$msg = '';
			// foreach term, add a reference in the posttag table
			foreach ($terms->ResultSet->Result as $result) {
				$this->db->where('name', $result);
				$this->db->where('id_vocabulary', $id_vocabulary);
				$query_term = $this->db->get('term');

				if( $query_term->num_rows() > 0 ) {
					$row_term = $query_term->row();
					$id_term = $row_term->id_term;
					$msg .= 'dup: '.$result.' - ';
				} else {
					$this->db->insert('term', array('name'=>$result,'id_vocabulary'=>$id_vocabulary));
					$id_term = $this->db->insert_id();
				}
				$this->db->insert('posttag',array('id_term'=>$id_term,'id_post'=>$row->id_post));
				log_message('error', ' --- ADD POSTTAG --- '.$result);
			}
			
			// and add a row in the cluster table
			$cluster_data = array('id_query'=>$id_query,'id_parent'=>$row->id_post,'terms_meta'=>$terms_json,'type'=>100,'meta'=>$row->meta);
			$this->db->insert('cluster',$cluster_data);
			echo $row->id_post.' ['.$msg.']<br/>';
		}

//		$events = $this->ted_read_events();
//		foreach( $events as $name => $event ) {
//			if( $name != 'undefined') {
//				foreach( $event as $id_post => $talk ) {
//					$query = $this->db->query('select fom_link.text as text, fom_post.id_post as id_post, fom_postlink.id_link from fom_link, fom_post, fom_postlink where fom_post.id_post = fom_postlink.id_post and fom_link.id_link = fom_postlink.id_link and fom_post.id_post = '.$id_post);
//					if( $query->num_rows() > 0 ) {
//						$row = $query->row();
//						$terms = $this->fom_analyzer->yahoo_extract( $row->text );
//						echo $terms;
//						$cluster_meta = json_encode( array('id_post'=>$id_post, 'event'=>$name,'speaker'=>$talk['speaker'],'url'=>$talk['url']) );
//						$cluster_data = array('id_query'=>$id_query,'id_parent'=>$id_post,'terms_meta'=>$terms,'type'=>100,'meta'=>$cluster_meta);
//						$this->db->insert('cluster',$cluster_data);
//					}
//				}
//			}
//		}
	}

	function ted_get_corpora()
	{
		$this->load->library('fom_analyzer');
		$permissions = $this->fom_analyzer->corpus_get_permissions();
		var_dump($permissions);
	}

	function ted_list_contexts()
	{
		$this->load->library('fom_analyzer');
		$contexts = $this->fom_analyzer->list_contexts();
		var_dump( $contexts );
	}

	function ted_extract_topics()
	{
		$this->load->library('fom_analyzer');
		
		$articles = $this->session->userdata('articles');
		
		foreach( $articles as $article ) {
			log_message('error', ' --- FETCHING TAGS --- '.$article);
			$tags[] = $this->fom_analyzer->get_entitytags($article);
		}
		
		$result = '';
		foreach( $tags as $key => $tag ) {
			$result .= 'TAGS['.$key.'] ';
			foreach ( $tag as $elem ) {
				$result .= $elem['tagWord'].' ('.$elem['relevance'].'), ';
			}
		}

		var_dump($result);
	}
	
	function ted_insert_talks( $event_name = '' )
	{
		$this->load->library('fom_analyzer');
		$events = $this->ted_read_events();
		$articles = array();

		foreach( $events as $name => $event ) {
			if( $name == $event_name ) {
				foreach( $event as $id_post => $talk ) {

					$uri = $talk['url'];
					$summary = $talk['content'];
					$transcript = '';
					$this->db->where('uri', $uri);
					$query = $this->db->get('link');
					if( $query->num_rows() > 0 ) {
						$row = $query->row();
						$transcript = $row->text;
					}

					log_message('error', ' --- ADDING --- '.$talk['speaker']);
					$article_id = $this->fom_analyzer->add_article($talk['title'], $transcript, $talk['url'], $talk['speaker'], 'en');
					$articles[] = $article_id;
				}
			}
		}
		$corpus['event_name'] = $event_name;
		$corpus['articles'] = $articles;
		
		$this->session->set_userdata( $corpus );
		$this->index();
	}
	
	function ted_killsaplo()
	{
		$this->load->library('fom_analyzer');
		$this->fom_analyzer->kill_saplosession();
		$this->session->unset_userdata( 'event_name' );
		$this->session->unset_userdata( 'articles' );
		$this->index();
	}
}
/* end of admin.php */