package fom.clustering.algorithms.hierarchical.metrics;


import fom.clustering.algorithms.hierarchical.HierarchicalCluster;
import fom.model.Post;

public class PostClusterGeoDistanceNoCache implements ClusterDistanceMeasure<Post> {
	
	@Override
	public double getDistance(HierarchicalCluster<Post> cluster1, HierarchicalCluster<Post> cluster2) {
		double maxDistance = 0;
		
		for(Post cluster1Post : cluster1.getObjects()){
			for(Post cluster2Post : cluster2.getObjects()){
				double lat1 = Math.toRadians(cluster1Post.getLat());
				double lon1 = Math.toRadians(cluster1Post.getLon());
				double lat2 = Math.toRadians(cluster2Post.getLat());
				double lon2 = Math.toRadians(cluster2Post.getLon());
				double distance =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*6371;
				if(distance>maxDistance){
					maxDistance = distance;
				}
			}
		}
		return maxDistance;
	}

}
