package fom.clustering.algorithms;

import fom.clustering.algorithms.kmedoids.KMedoidsClusterer;
import fom.clustering.algorithms.kmedoids.metrics.AbstractMetric;

public class ClustererFactory<ObjectType> {
	
	public Clusterer<ObjectType> kMedoidsClusterer(ObjectType[] objects, int k, AbstractMetric<ObjectType> metric, int maxIterations){
		return new KMedoidsClusterer<ObjectType>(objects, metric, k, maxIterations);
	}
	
}
