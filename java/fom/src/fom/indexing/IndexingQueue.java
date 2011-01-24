package fom.indexing;

import java.util.ArrayList;
import java.util.List;

import fom.model.Post;
import fom.properties.PropertyHandler;

public class IndexingQueue {
	
	private static IndexingQueue instance;
	private List<Post> postQueue;
	
	private IndexingQueue(){
		this.postQueue = new ArrayList<Post>();
	}
	
	public static IndexingQueue getInstance(){
		if(instance==null){
			instance = new IndexingQueue();
		}
		return instance;
	}
	
	public void addToQueue(Post post){
		postQueue.add(post);
		if(postQueue.size()==PropertyHandler.getIntProperty("BatchIndexingSize")){
			new Thread(new LuceneIndexer(postQueue.toArray(new Post[0]))).start();
			postQueue = new ArrayList<Post>();
		}
	}
}
