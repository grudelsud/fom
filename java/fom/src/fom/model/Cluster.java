package fom.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Cluster {
	private List<Post> posts;

	public abstract Map<String, String> getMeta();
	
	// public abstract List<Term> getClusterTerms; TODO!!!!
	
	public Cluster(){
		this.posts = new ArrayList<Post>();
	}
	
	public List<Post> getPosts(){
		return posts;
	}
	
	public void addPost(Post thePost){
		posts.add(thePost);
	}
	
}
