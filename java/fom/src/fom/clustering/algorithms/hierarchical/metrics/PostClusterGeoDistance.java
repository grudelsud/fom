package fom.clustering.algorithms.hierarchical.metrics;

import java.util.HashMap;
import java.util.Map;

import fom.clustering.algorithms.hierarchical.HierarchicalCluster;
import fom.model.Post;

public class PostClusterGeoDistance implements ClusterDistanceMeasure<Post> {

	private Map<Post, Map<Post, Double>> distancesCache;
	
	public PostClusterGeoDistance(){
		this.distancesCache = new HashMap<Post, Map<Post,Double>>();
	}
	
	@Override
	public double getDistance(HierarchicalCluster<Post> cluster1, HierarchicalCluster<Post> cluster2) {
		double maxDistance = 0;
		
		for(Post cluster1Post : cluster1.getObjects()){
			Map<Post, Double> distanceMap = distancesCache.get(cluster1Post);
			if(distanceMap==null){
				distanceMap = new HashMap<Post, Double>();
				distancesCache.put(cluster1Post, distanceMap);
			}
			for(Post cluster2Post : cluster2.getObjects()){
				Double distance = distanceMap.get(cluster2Post);
				if(distance==null){
					double lat1 = Math.toRadians(cluster1Post.getLat());
					double lon1 = Math.toRadians(cluster1Post.getLon());
					double lat2 = Math.toRadians(cluster2Post.getLat());
					double lon2 = Math.toRadians(cluster2Post.getLon());
					distance =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*6371;
					distanceMap.put(cluster2Post, distance);
				}
				if(distance>maxDistance){
					maxDistance = distance;
				}
			}
		}
		
		return maxDistance;
	}

}
