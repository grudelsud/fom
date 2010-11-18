package fom.clustering;

import java.util.ArrayList;
import java.util.List;

import fom.clustering.algorithms.Clusterer;
import fom.clustering.algorithms.ClustererFactory;
import fom.clustering.algorithms.kmedoids.metrics.AbstractMetric;
import fom.clustering.algorithms.kmedoids.metrics.PostGeoDistance;
import fom.model.GeoCluster;
import fom.model.Post;
import fom.model.Query;

public class GeoClustering {	
	private Clusterer<Post> clusterer;
	private List<Post> posts;
	private List<GeoCluster> clusters;
	private Query originatingQuery;
	
	public GeoClustering(Query originatingQuery, List<Post> posts){
		this.posts = new ArrayList<Post>();
		clusters = new ArrayList<GeoCluster>();
		
		this.originatingQuery = originatingQuery;
		this.posts = posts;
	}	
	
	public List<GeoCluster> performClustering(){
		
		GeoCluster notGeoTagged = new GeoCluster(originatingQuery, 0, 0, 0, 0);
		List<Post> toBeClustered = new ArrayList<Post>();
		
		for(Post post:posts){
			if(post.getLat()!=0 || post.getLon()!=0){
				toBeClustered.add(post);
			} else {
				notGeoTagged.addPost(post);
			}
		}
		
		System.out.println("Not geotagged: " + notGeoTagged.getPosts().size());
		System.out.println("Geotagged: " + toBeClustered.size());
		
		if(toBeClustered.size()>0){
			Post[] toBeClusteredArray = toBeClustered.toArray(new Post[0]);
			int k = (toBeClustered.size()/10)>0?(toBeClustered.size()/10):1;
			AbstractMetric<Post> metric = new PostGeoDistance();
			
			clusterer = ClustererFactory.getKMedoidsClusterer(toBeClusteredArray, k, metric, 1000);
			clusterer.performClustering();
			for(int clusterIndex=0; clusterIndex<k; clusterIndex++){
				GeoCluster currentCluster = new GeoCluster(originatingQuery);
				for(int postIndex=0; postIndex<toBeClustered.size(); postIndex++){
					if(clusterer.getClusterIndexes()[postIndex]==clusterIndex){
						currentCluster.addPost(toBeClusteredArray[postIndex]);
					}
				}
				clusters.add(currentCluster);
			}			
		}
		
		clusters.add(notGeoTagged);
		return clusters;
	}
}
