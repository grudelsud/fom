package fom.tests;

import java.util.ArrayList;

import org.joda.time.DateTime;

import fom.model.Post;
import fom.model.dao.DAOFactory;
import fom.search.Searcher;

public class SearchAndSave {

	public static void main(String[] args) {
		
		ArrayList<String> terms = new ArrayList<String>();
		terms.add("obama");
		terms.add("vote");
		
		Searcher searcher = new Searcher();
		searcher.addSource("Twitter");
		
		DateTime startTime = new DateTime().minusDays(0);
		DateTime endTime = new DateTime();
		
		for(Post post : searcher.search(terms, startTime, endTime)){
			DAOFactory.getFactory().getPostDAO().create(post);
			System.out.println("***SAVED POST***");
			System.out.println("Id: " + post.getId());
			System.out.println("Content: " + post.getContent());
			System.out.println("Created: " + post.getCreated().toString());
			System.out.println("TimeZone: " + post.getTimezone());
			System.out.println("Lat: " + post.getLat());
			System.out.println("Lon: " + post.getLon());
			System.out.println("Meta: " + post.getMeta().toString());
			System.out.println("Source: " + post.getSourceName());
			System.out.println("***********\n");
		}
		
		Post post = DAOFactory.getFactory().getPostDAO().retrieve(3);
		System.out.println("***RETRIEVED POST***");
		System.out.println("Id: " + post.getId());
		System.out.println("Content: " + post.getContent());
		System.out.println("Created: " + post.getCreated().toString());
		System.out.println("TimeZone: " + post.getTimezone());
		System.out.println("Lat: " + post.getLat());
		System.out.println("Lon: " + post.getLon());
		System.out.println("Meta: " + post.getMeta().toString());
		System.out.println("Source: " + post.getSourceName());
		System.out.println("***********\n");
		
	
	}

}
