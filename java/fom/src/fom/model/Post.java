package fom.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

public abstract class Post {
 
	private long id;
	private double lat;
	private double lon;
	private String content;
	private DateTime created;
	private DateTime modified;
	private int timezone;
	private Place location;
	private List<Media> media;
	private List<Term> terms;
	
	public abstract Map<String, String> getMeta();
	public abstract String getSourceName();

	public Post(long id, double lat, double lon, String content, DateTime created, DateTime modified, int timezone, Place location){
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.content = content;
		this.created = created;
		this.modified = modified;
		this.timezone = timezone;
		this.location = location;
		media = new ArrayList<Media>();
		terms = new ArrayList<Term>();
	}
	

	public long getId() {
		return id;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public String getContent() {
		return content;
	}

	public DateTime getCreated() {
		return created;
	}
	
	public DateTime getModified(){
		return modified;
	}
	
	public int getTimezone(){
		return timezone;
	}

	public Place getLocation() {
		return location;
	}
	
	
	public List<Media> getMedia(){
		return media;
	}
	
	public void addMedia(Media theMedia){
		this.media.add(theMedia);
	}
	
	public List<Term> getTerms(){
		return terms;
	}
	
	public void addTerm(Term theTerm){
		this.terms.add(theTerm);
	}
	
}
