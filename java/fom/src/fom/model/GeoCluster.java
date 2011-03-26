package fom.model;

import java.util.HashMap;
import java.util.Map;

public class GeoCluster extends Cluster {
	Query originatingQuery;

	public GeoCluster(Query originatingQuery, Cluster parent) {
		super(originatingQuery, parent);
		this.originatingQuery = originatingQuery;
	}

	public double getStdDevLat() {
		if(this.getPosts().size()==0){
			return 0;					//TODO: exception?
		}
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
		meta.put("meanLat", Double.toString(getMeanLat()));
		meta.put("meanLon", Double.toString(getMeanLon()));
		meta.put("stdDevLat", Double.toString(getStdDevLat()));
		meta.put("stdDevLon", Double.toString(getStdDevLon()));
		return meta;
	}

	@Override
	public int getTypeId() {
		return 2;
	}
}
