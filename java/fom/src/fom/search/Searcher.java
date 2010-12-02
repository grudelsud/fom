package fom.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;

import fom.model.Post;
import fom.search.sources.Source;
import fom.search.sources.SourceFactory;

public class Searcher {
	
	private Set<Source> sources;
	private List<Post> posts;
	
	public Searcher(){
		this.sources = new HashSet<Source>();
		this.posts = new ArrayList<Post>();
	}

	public void addSource(String sourceName){
		sources.add(SourceFactory.getSource(sourceName));
	}
	
	public List<Post> search(List<String> terms, DateTime startTime, DateTime endTime, double lat, double lon, int radius){
		System.out.println("Searching for posts matching the query " + terms + " within a " + radius + "km radius from (" + lat + "," + lon + ")");
		for(Source source : sources){
				posts.addAll(source.searchPosts(terms, startTime, endTime, lat, lon, radius));
		}
		System.out.println("Found " + posts.size() + " posts");
		return posts;
	}
}
