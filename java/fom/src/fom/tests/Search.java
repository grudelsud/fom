package fom.tests;

import java.util.ArrayList;

import org.joda.time.DateTime;

import fom.model.Post;
import fom.search.Searcher;

public class Search {

	public static void main(String[] args) {
		
		ArrayList<String> terms = new ArrayList<String>();
		terms.add("obama");
		terms.add("vote");
		
		Searcher searcher = new Searcher();
		searcher.addSource("Twitter");
		
		DateTime startTime = new DateTime().minusDays(1);
		DateTime endTime = new DateTime();
		
		for(Post post : searcher.search(terms, startTime, endTime)){
			System.out.println("***POST***");
			System.out.println("Content: " + post.getContent());
			System.out.println("Created: " + post.getCreated().toString());
			System.out.println("TimeZone: " + post.getCreated().getZone().toString());
			System.out.println("Lat: " + post.getLat());
			System.out.println("Lon: " + post.getLon());
			System.out.println("***********\n");
		}
		
		
	
	}

}
