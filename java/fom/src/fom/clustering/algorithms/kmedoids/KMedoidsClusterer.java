package fom.clustering.algorithms.kmedoids;


import fom.clustering.algorithms.Clusterer;
import fom.clustering.algorithms.kmedoids.jmlcore.Dataset;
import fom.clustering.algorithms.kmedoids.jmlcore.DefaultDataset;
import fom.clustering.algorithms.kmedoids.jmlcore.DenseInstance;
import fom.clustering.algorithms.kmedoids.jmlcore.Instance;
import fom.clustering.algorithms.kmedoids.metrics.AbstractMetric;

import java.util.HashMap;
import java.util.Map;



/**
 * A wrapper around the javaml library implementation of K-Medoids. Allows easy clustering of any set of objects given  a distance or similarity measure.
 * @author    Federico Frappi
 * @param  < ObjectType  >  The type of the objects to be clustered.
 */
public class KMedoidsClusterer<ObjectType> implements Clusterer<ObjectType> {
	private ObjectType[] objects;
	/**
	 * @uml.property  name="dataset"
	 * @uml.associationEnd  
	 */
	private Dataset dataset;
	/**
	 * @uml.property  name="clusters"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Dataset[] clusters = null;
	/**
	 * @uml.property  name="medoids"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Instance[] medoids = null;

	
	@SuppressWarnings("unused")
	private KMedoidsClusterer(){
		super();
	}
	
	/**
	 * @uml.property  name="algorithm"
	 * @uml.associationEnd  
	 */
	KMedoidsAlgorithm algorithm;
	
	public KMedoidsClusterer(ObjectType[] objects, AbstractMetric<ObjectType> distanceMeasure, int k, int maxIterations){
		this.objects = objects;
		distanceMeasure.setObjects(objects);
		dataset = new DefaultDataset();
		for(int i=0; i<objects.length; i++){
			double[] attr = {i};
			Instance tmpInstance = new DenseInstance(attr);
			dataset.add(tmpInstance);
		}
		this.algorithm = new KMedoidsAlgorithm(k, maxIterations, distanceMeasure);
	}
	
	
	/**
	 * 
	 * Performs the clustering.
	 * 
	 */
	public void performClustering(){
		clusters = algorithm.cluster(dataset);
		medoids = algorithm.getMedoids();
	}
	
	/**
	 * 
	 * Performs the clustering (if not already done) and returns 
	 * an array with the indices of the clusters for each item.
	 * 
	 * @return An array of integers, each entry is the cluster index for
	 * the corresponding item in the input objects array.
	 */
	public int[] getClusterIndexes(){
		int[] clusteringResult = new int[objects.length];
		if (clusters==null) performClustering();
		for(int currentClusterIndex=0; currentClusterIndex<clusters.length; currentClusterIndex++){
			Dataset currentCluster = clusters[currentClusterIndex];
			for(int currentIndexInCluster=0; currentIndexInCluster<currentCluster.size(); currentIndexInCluster++){
				int objectIndex = (int)currentCluster.instance(currentIndexInCluster).value(0);
				clusteringResult[objectIndex]=currentClusterIndex;
			}
		}
		return clusteringResult;
	}

	
	/**
	 * 
	 * Performs the clustering (if not already done) and returns 
	 * a map wich associates each clustered element with his cluster index.
	 * 
	 * @return A Map containing each clustered element and his cluster index.
	 */
	public Map<ObjectType, Integer> getClusterMap(){
		Map<ObjectType, Integer> clusteringResult = new HashMap<ObjectType, Integer>();
		if (clusters==null) performClustering();
		for(int currentClusterIndex=0; currentClusterIndex<clusters.length; currentClusterIndex++){
			Dataset currentCluster = clusters[currentClusterIndex];
			for(int currentIndexInCluster=0; currentIndexInCluster<currentCluster.size(); currentIndexInCluster++){
				int objectIndex = (int)currentCluster.instance(currentIndexInCluster).value(0);
				clusteringResult.put(objects[objectIndex], new Integer(currentClusterIndex));
			}
		}
		return clusteringResult;
	}
	
	public int[] getMedoidIndexes(){
		if(medoids==null) performClustering();
		int[] outMedoids = new int[medoids.length];
		for(int i=0; i<medoids.length; i++){
			outMedoids[i]=(int)medoids[i].value(0);
		}
		return outMedoids;
	}
	
	public Map<Integer, ObjectType> getMedoidMap(){
		Map<Integer, ObjectType> outMap = new HashMap<Integer, ObjectType>();
		if(medoids==null) performClustering();
		for(int i=0; i<medoids.length; i++){
			outMap.put(i, objects[(int)medoids[i].value(0)]);
		}
		return outMap;
	}
}
