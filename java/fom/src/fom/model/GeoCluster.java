package fom.model;

import java.util.Map;

public class GeoCluster extends Cluster {

	private double meanLat;
	private double meanLon;
	private double varLat;
	private double varLon;
	

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

}
