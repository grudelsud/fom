package fom.model;

import java.util.HashMap;
import java.util.Map;

import fom.model.dao.interfaces.DAOFactory;

public class GeoCluster extends Cluster {
	Query originatingQuery;

	public GeoCluster(Query originatingQuery, Cluster parent) {
		super(originatingQuery, parent);
		this.originatingQuery = originatingQuery;
	}
	
	public double getMeanLat() {
		if(this.getPosts().size()==0){
			return 0;					//TODO: exception?
		}
		/*
		if(this.getPosts().get(1).getLat()==0 && this.getPosts().get(1).getLon()==0){
			if(originatingQuery.getLat()!=0 || originatingQuery.getLon()!=0){ //If it's a geoQuery and this is the cluster with the posts without the coordinates
				return originatingQuery.getLat();
			}
		}
		*/
		double meanLat = 0;
		for(Post post: this.getPosts()){
			meanLat+=post.getLat();
		}
		meanLat = meanLat/this.getPosts().size();
		return meanLat;
	}


	public double getMeanLon() {
		if(this.getPosts().size()==0){
			return 0;					//TODO: exception?
		}
		/*
		if(this.getPosts().get(1).getLat()==0 && this.getPosts().get(1).getLon()==0){
			if(originatingQuery.getLat()!=0 || originatingQuery.getLon()!=0){ //If it's a geoQuery and this is the cluster with the posts without the coordinates
				return originatingQuery.getLon();
			}
		}
		*/
		double meanLon = 0;
		for(Post post: this.getPosts()){
			meanLon+=post.getLon();
		}
		meanLon = meanLon/this.getPosts().size();
		return meanLon;
	}


	public double getStdDevLat() {
		if(this.getPosts().size()==0){
			return 0;					//TODO: exception?
		}
		/*
		if(this.getPosts().get(1).getLat()==0 && this.getPosts().get(1).getLon()==0){
			if(originatingQuery.getLat()!=0 || originatingQuery.getLon()!=0){ //If it's a geoQuery and this is the cluster with the posts without the coordinates
				return originatingQuery.getRadius();
			}
		}
		*/
		if(this.getPosts().size()==1){
			return 0;
		}
		double variance = 0;
		int n=0;
		double sum=0;
		double squaresum=0;
		for(Post post:this.getPosts()){
			n++;
			sum+=post.getLat();
			squaresum+=post.getLat()*post.getLat();
		}
		variance = (squaresum - sum*(sum/n))/(n-1);
		return Math.sqrt(variance);
	}


	public double getStdDevLon() {
		if(this.getPosts().size()==0){
			return 0;					//TODO: exception?
		}
		/*
		if(this.getPosts().get(1).getLat()==0 && this.getPosts().get(1).getLon()==0){
			if(originatingQuery.getLat()!=0 || originatingQuery.getLon()!=0){ //If it's a geoQuery and this is the cluster with the posts without the coordinates
				return originatingQuery.getRadius();
			}
		}
		*/
		if(this.getPosts().size()==1){
			return 0;
		}		
		double variance = 0;
		int n=0;
		double sum=0;
		double squaresum=0;
		for(Post post:this.getPosts()){
			n++;
			sum+=post.getLon();
			squaresum+=post.getLon()*post.getLon();
		}
		variance = (squaresum - sum*(sum/n))/(n-1);
		return Math.sqrt(variance);
	}
	

	@Override
	public Map<String, String> getMeta() {
		Map<String, String> meta = new HashMap<String, String>();
		meta.put("type", "geo");
		meta.put("meanLat", Double.toString(getMeanLat()));
		meta.put("meanLon", Double.toString(getMeanLon()));
		meta.put("stdDevLat", Double.toString(getStdDevLat()));
		meta.put("stdDevLon", Double.toString(getStdDevLon()));
		long parentClusterID = this.getParentCluster().getId();
		if(parentClusterID==0){
			parentClusterID = DAOFactory.getFactory().getClusterDAO().create(getParentCluster());
		}
		meta.put("parent", Long.toString(parentClusterID));
		return meta;
	}
}
