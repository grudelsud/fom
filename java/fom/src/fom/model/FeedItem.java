package fom.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class FeedItem {
	private long id;
	private Feed feed;
	private String uri;
	private String title;
	private String description;
	private DateTime publishingDate;

	private List<Term> categories;

	public FeedItem(Feed feed, String title, String description, String uri, DateTime publishingDate){
		super();
		this.feed = feed;
		this.title = title;
		this.description = description;
		this.uri = uri;
		this.publishingDate = publishingDate;
		this.categories = new ArrayList<Term>();
	}

	public long getId() {
		return id;
	}
	
	public Feed getFeed(){
		return feed;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public DateTime getPublishingDate() {
		return publishingDate;
	}

	public List<Term> getCategories() {
		return categories;
	}
	
	public void addCategory(Term theCategory){
		this.categories.add(theCategory);
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}
	
	
}
