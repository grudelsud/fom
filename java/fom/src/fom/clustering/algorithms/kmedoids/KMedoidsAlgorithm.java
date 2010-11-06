/**
 * This file is part of the Java Machine Learning Library
 * 
 * The Java Machine Learning Library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * The Java Machine Learning Library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Java Machine Learning Library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * Copyright (c) 2006-2009, Thomas Abeel
 * 
 * Project: http://java-ml.sourceforge.net/
 * 
 */
package fom.clustering.algorithms.kmedoids;

import fom.clustering.algorithms.kmedoids.jmlcore.Dataset;
import fom.clustering.algorithms.kmedoids.jmlcore.DefaultDataset;
import fom.clustering.algorithms.kmedoids.jmlcore.DistanceMeasure;
import fom.clustering.algorithms.kmedoids.jmlcore.Instance;

import java.util.Random;


/**
 * Implementation of the K-medoids algorithm. K-medoids is a clustering algorithm that is very much like k-means. The main difference between the two algorithms is the cluster center they use. K-means uses the average of all instances in a cluster, while k-medoids uses the instance that is the closest to the mean, i.e. the most 'central' point of the cluster. Using an actual point of the data set to cluster makes the k-medoids algorithm more robust to outliers than the k-means algorithm. This is a modified version of the original class to support non-metric  spaces. It implements an approximated PAM (Partitioning Around Medoids)  clustering algorithm.
 * @author  Thomas Abeel, Federico Frappi
 */
class KMedoidsAlgorithm {
	/* Distance measure to measure the distance between instances */
	/**
	 * @uml.property  name="dm"
	 * @uml.associationEnd  
	 */
	private DistanceMeasure dm;

	/* Number of clusters to generate */
	private int numberOfClusters;

	/* Random generator for selection of candidate medoids */
	private Random rg;

	/* The maximum number of iterations the algorithm is allowed to run. */
	private int maxIterations;
	
	/**
	 * @uml.property  name="medoids"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Instance[] medoids;
	
	/**
	 * @uml.property  name="output"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Dataset[] output;
	
	private int currentCycleCount = 0;
	
	@SuppressWarnings("unused")
	private KMedoidsAlgorithm(){
		super();
	}

	/**
	 * Creates a new instance of the k-medoids algorithm with the specified
	 * parameters.
	 * 
	 * @param numberOfClusters
	 *            the number of clusters to generate
	 * @param maxIterations
	 *            the maximum number of iteration the algorithm is allowed to
	 *            run
	 * @param DistanceMeasure
	 *            dm the distance metric to use for measuring the distance
	 *            between instances
	 * 
	 */
	public KMedoidsAlgorithm(int numberOfClusters, int maxIterations, DistanceMeasure dm) {
		super();
		this.numberOfClusters = numberOfClusters;
		this.maxIterations = maxIterations;
		this.dm = dm;
		rg = new Random(System.currentTimeMillis());
	}

	public Dataset[] cluster(Dataset data) {
	//	long clusteringStartTime = System.currentTimeMillis();
	//	System.out.println("Clustering started...");
		medoids = new Instance[numberOfClusters];
		output = new DefaultDataset[numberOfClusters];
		for (int i = 0; i < numberOfClusters; i++) {
			int random = rg.nextInt(data.size());
			medoids[i] = data.instance(random);
		}
		boolean changed = true;
		while (changed && currentCycleCount < maxIterations) {
	//		long cycleStartTime = System.currentTimeMillis();
			changed = false;
			currentCycleCount++;
			int[] assignment = assign(medoids, data);
			changed = recalculateMedoids(assignment, medoids, output, data);
	//		System.out.println("\tCycle #" + currentCycleCount + " completed in " + ((System.currentTimeMillis()-cycleStartTime)/1000) + " seconds");
		}
	//	System.out.println("Clustering completed in " + ((System.currentTimeMillis()-clusteringStartTime)/1000) + " seconds.");
		return output;
	}

	/**
	 * Assign all instances from the data set to the medoids.
	 * 
	 * @param medoids candidate medoids
	 * @param data the data to assign to the medoids
	 * @return best cluster indices for each instance in the data set
	 */
	private int[] assign(Instance[] medoids, Dataset data) {
		int[] out = new int[data.size()];
		for (int i = 0; i < data.size(); i++) {
			double bestDistance = dm.measure(data.instance(i), medoids[0]);
			int bestIndex = 0;
			for (int j = 1; j < medoids.length; j++) {
				double tmpDistance = dm.measure(data.instance(i), medoids[j]);
				if (dm.compare(tmpDistance, bestDistance)) {
					bestDistance = tmpDistance;
					bestIndex = j;
				}
			}
			out[i] = bestIndex;
		}
		return out;

	}

	/**
	 * Return a array with on each position the clusterIndex to which the
	 * Instance on that position in the dataset belongs.
	 * 
	 * @param medoids
	 *            the current set of cluster medoids, will be modified to fit
	 *            the new assignment
	 * @param assigment
	 *            the new assignment of all instances to the different medoids
	 * @param output
	 *            the cluster output, this will be modified at the end of the
	 *            method
	 * @return the
	 */
	private boolean recalculateMedoids(int[] assignment, Instance[] medoids, Dataset[] output, Dataset data) {
		boolean changed = false;
		for (int i = 0; i < numberOfClusters; i++) {
			output[i] = new DefaultDataset();
			for (int j = 0; j < assignment.length; j++) {
				if (assignment[j] == i) {
					output[i].add(data.instance(j));
				}
			}
			if (output[i].size() == 0) { // && currentCycleCount!=maxIterations) { // new random, empty medoid
				medoids[i] = data.instance(rg.nextInt(data.size()));
				changed = true;		
			} else {
				double minCost = getConfigurationCost(medoids[i], output[i]);
				Instance minCostMedoid = medoids[i];
				for(int j=0; j<output[i].size(); j++){
					double otherConfigurationCost = getConfigurationCost(output[i].instance(j), output[i]);
					if(otherConfigurationCost<minCost){
						minCost = otherConfigurationCost;
						minCostMedoid = output[i].instance(j);
					}
				}
				if(!medoids[i].equals(minCostMedoid)){
					medoids[i]=minCostMedoid;
					changed = true;
				}
			}
		}
		return changed;
	}
	
	private double getConfigurationCost(Instance medoid, Dataset cluster){
		double cost=0;
		for(int i=0; i<cluster.size(); i++){
			cost+=dm.measure(cluster.instance(i), medoid);
		}
		return cost;
	}

	public Instance[] getMedoids(){
		return this.medoids;
	}
	
}
