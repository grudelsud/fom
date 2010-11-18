package fom.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class Query {
	private long id;
	private long userId;
	private String query;
	private DateTime startTime;
	private DateTime endTime;
	private String timeGranularity;
	private double lat;
	private double lon;
	private String geoGranularity;
	private DateTime created;
	private int timezone;
	private List<Cluster> clusters;
	private List<Term> terms;
	
	
	
	public Query(long id, long userId, String query, DateTime startTime, DateTime endTime, String timeGranularity, double lat, double lon, String geoGranularity, DateTime created, int timezone) {
		super();
		this.id = id;
		this.userId = userId;
		this.query = query;
		this.startTime = startTime;
		this.endTime = endTime;
		this.timeGranularity = timeGranularity;
		this.lat = lat;
		this.lon = lon;
		this.geoGranularity = geoGranularity;
		this.created = created;
		this.timezone = timezone;
		this.clusters = new ArrayList<Cluster>();
		this.terms = new ArrayList<Term>();
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getUserId(){
		return userId;
	}
	
	public String getQuery(){
		return query;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public String getTimeGranularity() {
		return timeGranularity;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public String getGeoGranularity() {
		return geoGranularity;
	}

	public DateTime getCreated() {
		return created;
	}

	public int getTimezone() {
		return timezone;
	}

	public List<Cluster> getClusters() {
		return clusters;
	}
	
	public void addCluster(Cluster theCluster) {
		this.clusters.add(theCluster);
	}
	
	public List<Term> getTerms() {
		return terms;
	}
	
	public void addTerm(Term theTerm) {
		this.terms.add(theTerm);
	}
}
