package fom.model;

import org.joda.time.DateTime;


public class Post {
 
	private long id;
	private long userId;
	private long placeId;
	private double lat;
	private double lon;
	private String content;
	private DateTime created;
	
	public Post(double lat, double lon, String content, DateTime created){
		this.lat = lat;
		this.lon = lon;
		this.content = content;
		this.created = created;
	}
	

	public long getId() {
		return id;
	}

	public long getUserId() {
		return userId;
	}

	public long getPlaceId() {
		return placeId;
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
	
}
