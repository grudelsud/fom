package fom.model;

public class Place {
	private double lat;
	private double lon;
	private String description;
	private String granularity;
	
	public Place(double lat, double lon, String description, String granularity) {
		super();
		this.lat = lat;
		this.lon = lon;
		this.description = description;
		this.granularity = granularity;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public String getDescription() {
		return description;
	}

	public String getGranularity() {
		return granularity;
	}

}
