package fom.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Cluster {
	private Query originatingQuery;
	private List<Post> posts;
	private List<Term> terms;

	public abstract Map<String, String> getMeta();
	
	public Cluster(Query originatingQuery){
		this.posts = new ArrayList<Post>();
		this.terms = new ArrayList<Term>();
		this.originatingQuery = originatingQuery;
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
	
}
