package fom.clustering.algorithms.kmedoids;


import fom.clustering.algorithms.Clusterer;
import fom.clustering.algorithms.kmedoids.jmlcore.Dataset;
import fom.clustering.algorithms.kmedoids.jmlcore.DefaultDataset;
import fom.clustering.algorithms.kmedoids.jmlcore.DenseInstance;
import fom.clustering.algorithms.kmedoids.jmlcore.Instance;
import fom.clustering.algorithms.kmedoids.metrics.AbstractMetric;

import java.util.ArrayList;
import java.util.List;



/**
 * A wrapper around the javaml library implementation of K-Medoids. Allows easy clustering of any set of objects given  a distance or similarity measure.
 * @author    Federico Frappi
 * @param  < ObjectType  >  The type of the objects to be clustered.
 */
public class KMedoidsClusterer<ObjectType> implements Clusterer<ObjectType> {
	private Dataset dataset;
	private Dataset[] clusters = null;
	
	KMedoidsAlgorithm algorithm;
	private AbstractMetric<ObjectType> distanceMeasure;
	private int k;
	private int maxIterations;
	
	public KMedoidsClusterer(AbstractMetric<ObjectType> distanceMeasure, int k, int maxIterations){
		this.distanceMeasure = distanceMeasure;
		this.k = k;
		this.maxIterations = maxIterations;
	}
	
	
	/**
	 * 
	 * Performs the clustering.
	 * 
	 */
	public List<List<ObjectType>> performClustering(List<ObjectType> data){
		List<List<ObjectType>> results = new ArrayList<List<ObjectType>>();
		dataset = new DefaultDataset();
		for(int i=0; i<data.size(); i++){
			double[] attr = {i};
			Instance tmpInstance = new DenseInstance(attr);
			dataset.add(tmpInstance);
		}
		this.algorithm = new KMedoidsAlgorithm(k, maxIterations, distanceMeasure);
		clusters = algorithm.cluster(dataset);
		
		for(int clusterIndex=0; clusterIndex<clusters.length; clusterIndex++){
			List<ObjectType> resultCurrentCluster = new ArrayList<ObjectType>();
			Dataset currentCluster = clusters[clusterIndex];
			for(int currentIndexInCluster=0; currentIndexInCluster<currentCluster.size(); currentIndexInCluster++){
				int objectIndex = (int)currentCluster.instance(currentIndexInCluster).value(0);
				resultCurrentCluster.add(data.get(objectIndex));
			}
			results.add(resultCurrentCluster);
		}
		
		return results;
	}
}
