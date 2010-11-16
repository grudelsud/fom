package fom.model;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class Query {
	private String query;
	private DateTime startTime;
	private DateTime endTime;
	private String timeGranularity;
	private long lat;
	private long lon;
	private String geoGranularity;
	private DateTime created;
	private int timezone;
	private List<Cluster> clusters;
	private List<Term> terms;
	
	
	
	public Query(String query, DateTime startTime, DateTime endTime, String timeGranularity, long lat, long lon, String geoGranularity, DateTime created, int timezone) {
		super();
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
	
	public String getQuery(){
		return query;
	}
	public void setQuery(String query){
		this.query = query;
	}
	public DateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}
	public DateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}
	public String getTimeGranularity() {
		return timeGranularity;
	}
	public void setTimeGranularity(String timeGranularity) {
		this.timeGranularity = timeGranularity;
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
	public String getGeoGranularity() {
		return geoGranularity;
	}
	public void setGeoGranularity(String geoGranularity) {
		this.geoGranularity = geoGranularity;
	}
	public DateTime getCreated() {
		return created;
	}
	public void setCreated(DateTime created) {
		this.created = created;
	}
	public int getTimezone() {
		return timezone;
	}
	public void setTimezone(int timezone) {
		this.timezone = timezone;
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
