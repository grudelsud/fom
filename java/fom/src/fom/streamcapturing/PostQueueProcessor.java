package fom.streamcapturing;

import java.util.concurrent.BlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import fom.model.Link;
import fom.model.Post;
import fom.model.dao.interfaces.PostDAO;
import fom.model.dao.localdb.LocalDBDAOFactory;
import fom.utils.BlacklistChecker;
import fom.utils.StringOperations;

public class PostQueueProcessor implements Runnable {

	private BlockingQueue<Post> postQueue;
	private PostDAO postDAO;
	
	public PostQueueProcessor(BlockingQueue<Post> postQueue){
		this.postQueue = postQueue;
		postDAO = LocalDBDAOFactory.getFactory().getPostDAO();
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
							currentPost.addLink(new Link(doc.baseUri(), doc.body().text()));							
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
