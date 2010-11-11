package fom.model;

import java.util.List;

import org.joda.time.DateTime;

public class Query {
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
}
