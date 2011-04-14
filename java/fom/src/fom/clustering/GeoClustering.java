package fom.clustering;

import java.util.ArrayList;
import java.util.List;

import fom.clustering.algorithms.Clusterer;
import fom.clustering.algorithms.ClustererFactory;
import fom.clustering.algorithms.hierarchical.metrics.ClusterDistanceMeasure;
import fom.clustering.algorithms.hierarchical.metrics.GeoClusterApproxCompleteLinkage;
import fom.model.Cluster;
import fom.model.GeoCluster;
import fom.model.Post;
import fom.model.Query;

public class GeoClustering {	
	private Cluster parentCluster;
	private Clusterer clusterer;
	private List<Post> posts;
	private List<GeoCluster> clusters;
	private String granularity;
	private Query originatingQuery;
	
	public GeoClustering(Query originatingQuery, List<Post> posts, String granularity, Cluster parentCluster){
		this.posts = new ArrayList<Post>();
		this.clusters = new ArrayList<GeoCluster>();
		this.granularity = granularity;
		this.originatingQuery = originatingQuery;
		this.posts = posts;
		this.parentCluster = parentCluster;
	}	
	
	public List<GeoCluster> performClustering(){
		
		System.out.println("Geoclustering...");
		GeoCluster notGeoTagged = new GeoCluster(originatingQuery, parentCluster);
		List<Post> toBeClustered = new ArrayList<Post>();
		
		for(Post post:posts){
			if(post.getLat()!=0 || post.getLon()!=0){
				toBeClustered.add(post);
			} else {
				notGeoTagged.addPost(post);
			}
		}
		
		if(toBeClustered.size()>0){
			double distLimit = 0;
			if(granularity.equalsIgnoreCase("poi")){
				distLimit = 1;
			} else if(granularity.equalsIgnoreCase("neighborhood")){
				distLimit = 3;
			} else if(granularity.equalsIgnoreCase("city")){
				distLimit = 10;
			} else {
				distLimit = Integer.parseInt(granularity);
			}
			ClusterDistanceMeasure distMeasure = new GeoClusterApproxCompleteLinkage();
			clusterer = ClustererFactory.getGreedyHAC(distMeasure, distLimit);

			List<List<Post>> clusteringResult = clusterer.performClustering(toBeClustered);
			for(List<Post> cluster : clusteringResult){
				GeoCluster currentCluster = new GeoCluster(originatingQuery, parentCluster);
				for(Post post : cluster){
					currentCluster.addPost(post);					
				}
				clusters.add(currentCluster);
			}			
		}
		if(notGeoTagged.getPosts().size()!=0){
			clusters.add(notGeoTagged);			
		}
		return clusters;
	}
}
