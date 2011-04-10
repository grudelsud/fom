package fom.streamcapturing;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import fom.langidentification.LanguageIdentifier;
import fom.langidentification.LanguageIdentifier.Language;
import fom.langidentification.Lc4jLangIdentifier;
import fom.model.Link;
import fom.model.Post;
import fom.model.dao.interfaces.PostDAO;
import fom.model.dao.localdb.LocalDBDAOFactory;
import fom.utils.BlacklistChecker;
import fom.utils.StringOperations;

public class PostQueueProcessor implements Runnable {

	private BlockingQueue<Post> postQueue;
	private PostDAO postDAO;
	private LanguageIdentifier langIdentifier;
	
	public PostQueueProcessor(BlockingQueue<Post> postQueue){
		this.postQueue = postQueue;
		this.postDAO = LocalDBDAOFactory.getFactory().getPostDAO();
		this.langIdentifier = new Lc4jLangIdentifier();
	}	
	
	@Override
	public void run() {
		while(true){
			try {
				Post currentPost = postQueue.take();
				for(String link : StringOperations.extractURLs(currentPost.getContent())){
					try {
						Document doc = Jsoup.connect(link).get();
						if(!BlacklistChecker.isBlacklisted(doc.baseUri())){
							Language lang = langIdentifier.identifyLanguageOf(doc.body().text());
							
							Map<String, String> meta = new HashMap<String, String>();
							
							for(Element e : doc.head().getElementsByTag("meta")){
								for(Attribute a : e.attributes()){
									if(a.getKey().equalsIgnoreCase("property")){
										if(a.getValue().startsWith("og:")){
											meta.put(a.getValue(), e.attr("content"));
										}
									} else if (a.getKey().equalsIgnoreCase("name")) {
										if(a.getValue().equalsIgnoreCase("keywords")){
											meta.put(a.getValue(), e.attr("content"));
										}
									} else if (a.getKey().equalsIgnoreCase("name")) {
										if(a.getValue().equalsIgnoreCase("description")){
											meta.put(a.getValue(), e.attr("content"));
										}
									}
								}
							}
														
							currentPost.addLink(new Link(doc.baseUri(), doc.body().text(), lang, meta));							
						}
					} catch (Exception e) {
						System.err.println("Invalid link found");
					}
				}			
				postDAO.create(currentPost);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
}
