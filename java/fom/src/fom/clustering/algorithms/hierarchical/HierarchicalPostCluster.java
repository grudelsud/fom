package fom.clustering.algorithms.hierarchical;

import java.util.ArrayList;
import java.util.List;

import fom.model.Post;

public class HierarchicalPostCluster {
	private ArrayList<Post> clusterObjects;
	private double centerLat;
	private double centerLon;
	private double radius;

	public HierarchicalPostCluster(Post firstObject){
		this.clusterObjects = new ArrayList<Post>();
		clusterObjects.add(firstObject);
		this.centerLat = firstObject.getLat();
		this.centerLon = firstObject.getLon();
		this.radius = 0;
	}
	
	public void mergeWith(HierarchicalPostCluster cluster){
		clusterObjects.ensureCapacity(cluster.getObjects().size());
		
		double lat1 = Math.toRadians(centerLat);
		double lon1 = Math.toRadians(centerLon);
		double lat2 = Math.toRadians(cluster.getCenterLat());
		double lon2 = Math.toRadians(cluster.getCenterLon());
		double d =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*6371;
		
		double r = Math.min(radius, cluster.radius);
		double R = Math.max(radius, cluster.radius);
		
		this.radius = Math.max(R, (d+r+R)/2);
		
		this.centerLat = (centerLat*clusterObjects.size() + cluster.centerLat*cluster.getObjects().size())/(clusterObjects.size() + cluster.getObjects().size());
		this.centerLat = (centerLon*clusterObjects.size() + cluster.centerLon*cluster.getObjects().size())/(clusterObjects.size() + cluster.getObjects().size());
		

		clusterObjects.addAll(cluster.getObjects());
	}
	
	public List<Post> getObjects(){
		return clusterObjects;
	}
	
	public double getCenterLat(){
		return centerLat;
	}
	
	public double getCenterLon(){
		return centerLon;
	}
	
	public double getRadius(){
		return radius;
	}
}
