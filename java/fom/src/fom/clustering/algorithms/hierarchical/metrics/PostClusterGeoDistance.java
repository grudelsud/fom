package fom.clustering.algorithms.hierarchical.metrics;


import fom.clustering.algorithms.hierarchical.HierarchicalCluster;
import fom.model.Post;
import gnu.trove.map.hash.TLongDoubleHashMap;
import gnu.trove.map.hash.TLongObjectHashMap;

public class PostClusterGeoDistance implements ClusterDistanceMeasure<Post> {

	private TLongObjectHashMap<TLongDoubleHashMap> distancesCache = new TLongObjectHashMap<TLongDoubleHashMap>();
	
	@Override
	public double getDistance(HierarchicalCluster<Post> cluster1, HierarchicalCluster<Post> cluster2) {
		double maxDistance = 0;
		
		for(Post cluster1Post : cluster1.getObjects()){

			TLongDoubleHashMap distanceMap = distancesCache.get(cluster1Post.getSourceId());
			if(distanceMap==null){
				distanceMap = new TLongDoubleHashMap();
				distancesCache.put(cluster1Post.getSourceId(), distanceMap);
			}
			for(Post cluster2Post : cluster2.getObjects()){
				double distance = distanceMap.get(cluster2Post.getSourceId());
				if(distance==0){
					double lat1 = Math.toRadians(cluster1Post.getLat());
					double lon1 = Math.toRadians(cluster1Post.getLon());
					double lat2 = Math.toRadians(cluster2Post.getLat());
					double lon2 = Math.toRadians(cluster2Post.getLon());
					distance =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*6371;
					distanceMap.put(cluster2Post.getSourceId(), distance);
				}
				if(distance>maxDistance){
					maxDistance = distance;
				}
			}
		}
		return maxDistance;
	}

}
