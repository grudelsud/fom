package fom.model;

import java.util.HashMap;
import java.util.Map;

public class GeoCluster extends Cluster {

	private double meanLat;
	private double meanLon;
	private double varLat;
	private double varLon;

	public GeoCluster(Query originatingQuery) {
		super(originatingQuery);
	}

	public GeoCluster(Query originatingQuery, double meanLat, double meanLon, double varLat, double varLon) {
		super(originatingQuery);
		this.meanLat = meanLat;
		this.meanLon = meanLon;
		this.varLat = varLat;
		this.varLon = varLon;
	}

	public double getMeanLat() {
		return meanLat;
	}


	public double getMeanLon() {
		return meanLon;
	}


	public double getVarLat() {
		return varLat;
	}


	public double getVarLon() {
		return varLon;
	}
	

	@Override
	public Map<String, String> getMeta() {
		Map<String, String> meta = new HashMap<String, String>();
		meta.put("type", "geo");
		meta.put("meanLat", new Double(getMeanLat()).toString());
		meta.put("meanLon", new Double(getMeanLon()).toString());
		meta.put("varLat", new Double(getVarLat()).toString());
		meta.put("varLon", new Double(getVarLon()).toString());
		return meta;
	}
}
