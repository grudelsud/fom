package fom.clustering.algorithms;

import fom.clustering.algorithms.hierarchical.HierarchicalAgglomerativeClusterer;
import fom.clustering.algorithms.hierarchical.metrics.ClusterDistanceMeasure;
import fom.clustering.algorithms.kmedoids.KMedoidsClusterer;
import fom.clustering.algorithms.kmedoids.metrics.AbstractMetric;

public class ClustererFactory<ObjectType> {
	
	public static<ObjectType> Clusterer<ObjectType> getHAClusterer(ClusterDistanceMeasure<ObjectType> distMeasure, double distLimit) {
		return new HierarchicalAgglomerativeClusterer<ObjectType>(distMeasure, distLimit);
	} 
	
	public static<ObjectType> Clusterer<ObjectType> getKMedoidsClusterer(int k, AbstractMetric<ObjectType> metric, int maxIterations){
		return new KMedoidsClusterer<ObjectType>(metric, k, maxIterations);
	}
	
}
