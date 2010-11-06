package fom.clustering.algorithms;

import fom.clustering.algorithms.kmedoids.KMedoidsClusterer;
import fom.clustering.algorithms.kmedoids.metrics.AbstractMetric;

public class ClustererFactory<ObjectType> {
	
	private ObjectType[] objects;
	
	public ClustererFactory(ObjectType[] objects){
		this.objects = objects;
	}
	
	public Clusterer<ObjectType> kMedoidsClusterer(int k, AbstractMetric<ObjectType> metric, int maxIterations){
		return new KMedoidsClusterer<ObjectType>(objects, metric, k, maxIterations);
	}
	
}
