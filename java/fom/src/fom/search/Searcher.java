package fom.search;


import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import fom.model.Post;
import fom.search.sources.Source;

public class Searcher {
	
	List<Source> sources;
	List<Post> posts;
	
	public Searcher(){
		this.sources = new ArrayList<Source>();
		this.posts = new ArrayList<Post>();
	}

	public void addSource(Source theSource){
		sources.add(theSource);
	}
	
	public List<Post> search(List<String> terms, DateTime startTime, DateTime endTime){
		for(Source source : sources){
			for(Post searchResult : source.searchPosts(terms, startTime, endTime)){
				posts.add(searchResult);
			}
		}
		return posts;
	}
	
}
