package fom.clustering.algorithms;

import java.util.Map;

public interface Clusterer<ObjectType> {
	
	/**
	 * 
	 * Performs the clustering.
	 * 
	 */
	public void performClustering();
	
	/**
	 * 
	 * Performs the clustering (if not already done) and returns 
	 * an array with the indexes of the clusters for each item.
	 * 
	 * @return An array of integers, each entry is the cluster index for
	 * the corresponding item in the input objects array.
	 */
	public int[] getClusterIndexes();
	
	/**
	 * 
	 * Performs the clustering (if not already done) and returns 
	 * a map which associates each clustered element with his cluster index.
	 * 
	 * @return A Map containing each clustered element and his cluster index.
	 */
	public Map<ObjectType, Integer> getClusterMap();
	
	public int[] getMedoidIndexes();
	
	public Map<Integer, ObjectType> getMedoidMap();
	
	
}
