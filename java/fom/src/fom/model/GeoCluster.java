package fom.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GeoCluster extends Cluster {

	private double meanLat;
	private double meanLon;
	private double varLat;
	private double varLon;

	public GeoCluster(double meanLat, double meanLon, double varLat,
			double varLon) {
		super();
		this.meanLat = meanLat;
		this.meanLon = meanLon;
		this.varLat = varLat;
		this.varLon = varLon;
	}


	public void setMeanLat(double meanLat) {
		this.meanLat = meanLat;
	}


	public void setMeanLon(double meanLon) {
		this.meanLon = meanLon;
	}


	public void setVarLat(double varLat) {
		this.varLat = varLat;
	}


	public void setVarLon(double varLon) {
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
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Term> getClusterTerms() {
		return new ArrayList<Term>();
	}

}
