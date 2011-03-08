package fom.clustering.algorithms;

import fom.clustering.algorithms.hierarchical.GreedyHAC;
import fom.clustering.algorithms.hierarchical.HAC;
import fom.clustering.algorithms.hierarchical.MoreGreedyHAC;
import fom.clustering.algorithms.hierarchical.metrics.ClusterDistanceMeasure;
import fom.clustering.algorithms.kmedoids.KMedoidsClusterer;
import fom.clustering.algorithms.kmedoids.metrics.AbstractMetric;
import fom.model.Post;

public class ClustererFactory<ObjectType> {
	
	public static Clusterer getHAC(ClusterDistanceMeasure distMeasure, double distLimit) {
		return new HAC(distMeasure, distLimit);
	} 
	
	public static Clusterer getGreedyHAC(ClusterDistanceMeasure distMeasure, double distLimit) {
		return new GreedyHAC(distMeasure, distLimit);
	}
	
	public static Clusterer getMoreGreedyHAC(ClusterDistanceMeasure distMeasure, double distLimit) {
		return new MoreGreedyHAC(distMeasure, distLimit);
	}
	
	public static Clusterer getKMedoidsClusterer(int k, AbstractMetric<Post> metric, int maxIterations){
		return new KMedoidsClusterer(metric, k, maxIterations);
	}
	
}
