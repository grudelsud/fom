package fom.model;

import java.util.HashMap;
import java.util.Map;

public class GeoCluster extends Cluster {
	

	public GeoCluster(Query originatingQuery) {
		super(originatingQuery);
	}
	
	public double getMeanLat() {
		if(this.getPosts().size()==0)
			return 0;					//TODO: exception?

		double meanLat = 0;
		for(Post post: this.getPosts()){
			meanLat+=post.getLat();
		}
		meanLat = meanLat/this.getPosts().size();
		return meanLat;
	}


	public double getMeanLon() {
		if(this.getPosts().size()==0)
			return 0;					//TODO: exception?

		double meanLon = 0;
		for(Post post: this.getPosts()){
			meanLon+=post.getLon();
		}
		meanLon = meanLon/this.getPosts().size();
		return meanLon;
	}


	public double getStdDevLat() {
		if(this.getPosts().size()<=1)
			return 0;					//TODO: exception?
		
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
		if(this.getPosts().size()<=1)
			return 0;					//TODO: exception?

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
		meta.put("meanLat", new Double(getMeanLat()).toString());
		meta.put("meanLon", new Double(getMeanLon()).toString());
		meta.put("stdDevLat", new Double(getStdDevLat()).toString());
		meta.put("stdDevLon", new Double(getStdDevLon()).toString());
		return meta;
	}
}
