package fom.model;

public class Place {
	private long lat;
	private long lon;
	private String description;
	private String granularity;
	
	public Place(long lat, long lon, String description, String granularity) {
		super();
		this.lat = lat;
		this.lon = lon;
		this.description = description;
		this.granularity = granularity;
	}

	public long getLat() {
		return lat;
	}

	public void setLat(long lat) {
		this.lat = lat;
	}

	public long getLon() {
		return lon;
	}

	public void setLon(long lon) {
		this.lon = lon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGranularity() {
		return granularity;
	}

	public void setGranularity(String granularity) {
		this.granularity = granularity;
	}

}
