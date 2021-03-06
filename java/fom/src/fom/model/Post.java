package fom.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import fom.langidentification.LanguageIdentifier.Language;

public abstract class Post {
 
	private long id;
	private double lat;
	private double lon;
	private String content;
	private DateTime created;
	private DateTime modified;
	private int timezone;
	private Place place;
	private List<Media> media;
	private List<Term> terms;
	private List<Link> links;
	private String userLocation;
	private boolean coordinatesEstimated;
	private Language language;
	
	public abstract Map<String, String> getMeta();
	public abstract String getSourceName();
	public abstract long getSourceId();

	public Post(long id, double lat, double lon, String content, DateTime created, DateTime modified, int timezone, Place place, String userLocation, boolean coordinatesEstimated, Language lang){
		this.id = id;
		this.lat = lat;
		this.lon = lon;
		this.content = content;
		this.created = created;
		this.modified = modified;
		this.timezone = timezone;
		this.place = place;
		this.userLocation = userLocation;
		this.coordinatesEstimated = coordinatesEstimated;
		this.media = new ArrayList<Media>();
		this.terms = new ArrayList<Term>();
		this.links = new ArrayList<Link>();
		this.language = lang;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Place getPlace() {
		return place;
	}
	
	public String getUserLocation(){
		return userLocation;
	}
	
	public boolean areCoordinatesEstimated(){
		return coordinatesEstimated;
	}
	
	public List<Link> getLinks(){
		return links;
	}
	
	public void addLink(Link theLink){
		this.links.add(theLink);
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
	
	public Language getLanguage(){
		return language;
	}
	
}
