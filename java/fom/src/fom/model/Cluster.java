package fom.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Cluster {
	private Cluster parentCluster;
	private long id;
	private Query originatingQuery;
	private List<Post> posts;
	private List<Term> terms;

	public abstract Map<String, String> getMeta();
	public abstract int getTypeId();
	
	public Cluster(Query originatingQuery, Cluster parent){
		this.posts = new ArrayList<Post>();
		this.terms = new ArrayList<Term>();
		this.originatingQuery = originatingQuery;
		this.setParentCluster(parent);
	}
	
	public List<Post> getPosts(){
		return posts;
	}
	
	public void addPost(Post thePost){
		posts.add(thePost);
	}
	
	public List<Term> getTerms(){
		return terms;
	}
	
	public void addTerm(Term theTerm){
		terms.add(theTerm);
	}
	
	public Query getOriginatingQuery() {
		return originatingQuery;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setParentCluster(Cluster parentCluster) {
		this.parentCluster = parentCluster;
	}

	public Cluster getParentCluster() {
		return parentCluster;
	}
	
	public double getMeanLat() {
		if(this.getPosts().size()==0){
			return 0;					//TODO: exception?
		}
		double meanLat = 0;
		for(Post post: this.getPosts()){
			meanLat+=post.getLat();
		}
		meanLat = meanLat/this.getPosts().size();
		return meanLat;
	}


	public double getMeanLon() {
		if(this.getPosts().size()==0){
			return 0;					//TODO: exception?
		}
		double meanLon = 0;
		for(Post post: this.getPosts()){
			meanLon+=post.getLon();
		}
		meanLon = meanLon/this.getPosts().size();
		return meanLon;
	}
}
