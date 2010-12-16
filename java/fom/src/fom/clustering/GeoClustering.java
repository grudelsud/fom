package fom.clustering;

import java.util.ArrayList;
import java.util.List;

import fom.clustering.algorithms.Clusterer;
import fom.clustering.algorithms.ClustererFactory;
import fom.clustering.algorithms.hierarchical.metrics.ClusterDistanceMeasure;
import fom.clustering.algorithms.hierarchical.metrics.PostClusterGeoDistance;
import fom.model.GeoCluster;
import fom.model.Post;
import fom.model.Query;

public class GeoClustering {	
	private Clusterer<Post> clusterer;
	private List<Post> posts;
	private List<GeoCluster> clusters;
	private String granularity;
	private Query originatingQuery;
	
	public GeoClustering(Query originatingQuery, List<Post> posts, String granularity){
		this.posts = new ArrayList<Post>();
		clusters = new ArrayList<GeoCluster>();
		this.granularity = granularity;
		this.originatingQuery = originatingQuery;
		this.posts = posts;
	}	
	
	public List<GeoCluster> performClustering(){
		
		GeoCluster notGeoTagged = new GeoCluster(originatingQuery);
		List<Post> toBeClustered = new ArrayList<Post>();
		
		for(Post post:posts){
			if(post.getLat()!=0 || post.getLon()!=0){
				toBeClustered.add(post);
			} else {
				notGeoTagged.addPost(post);
			}
		}
		
		if(toBeClustered.size()>0){
			double distLimit = 10;
			if(granularity.equalsIgnoreCase("POI")){
				distLimit = 1;
			} else if(granularity.equalsIgnoreCase("neighborhood")){
				distLimit = 3;
			} else if(granularity.equalsIgnoreCase("city")){
				distLimit = 10;
			}
			ClusterDistanceMeasure<Post> distMeasure = new PostClusterGeoDistance();
			clusterer = ClustererFactory.getHAClusterer(distMeasure, distLimit);

			List<List<Post>> clusteringResult = clusterer.performClustering(toBeClustered);
			for(List<Post> cluster : clusteringResult){
				GeoCluster currentCluster = new GeoCluster(originatingQuery);
				for(Post post : cluster){
					currentCluster.addPost(post);					
				}
				clusters.add(currentCluster);
			}			
		}
		
		clusters.add(notGeoTagged);
		return clusters;
	}
}
