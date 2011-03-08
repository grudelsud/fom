package fom.clustering.algorithms.hierarchical.metrics;

import fom.clustering.algorithms.hierarchical.HierarchicalPostCluster;

public class GeoClusterApproxCompleteLinkage implements ClusterDistanceMeasure {

	@Override
	public double getDistance(HierarchicalPostCluster cluster1, HierarchicalPostCluster cluster2) {
				
		double lat1 = Math.toRadians(cluster1.getCenterLat());
		double lon1 = Math.toRadians(cluster1.getCenterLon());
		double lat2 = Math.toRadians(cluster2.getCenterLat());
		double lon2 = Math.toRadians(cluster2.getCenterLon());
		double d =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*6371;

		double r = Math.min(cluster1.getRadius(), cluster2.getRadius());
		double R = Math.max(cluster1.getRadius(), cluster2.getRadius());
		
		return Math.max(R, (d+r+R)/2);
		
	}

}
