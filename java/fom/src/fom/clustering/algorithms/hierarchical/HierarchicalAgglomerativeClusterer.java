package fom.clustering.algorithms.hierarchical;

import java.util.ArrayList;
import java.util.List;

import fom.clustering.algorithms.Clusterer;
import fom.clustering.algorithms.DistanceMeasure;

public class HierarchicalAgglomerativeClusterer<ObjectType> implements Clusterer<ObjectType> {

	double limit;
	DistanceMeasure<HierarchicalCluster<ObjectType>> distMeasure;
	
	public HierarchicalAgglomerativeClusterer(DistanceMeasure<HierarchicalCluster<ObjectType>> distMeasure, double limit){
		this.distMeasure = distMeasure;
		this.limit = limit;
	}
		
	@Override
	public List<List<ObjectType>> performClustering(List<ObjectType> data){
		List<List<ObjectType>> results = new ArrayList<List<ObjectType>>();
		boolean change = true;
		List<HierarchicalCluster<ObjectType>> clusters = new ArrayList<HierarchicalCluster<ObjectType>>();
		for(ObjectType obj : data){
			clusters.add(new HierarchicalCluster<ObjectType>(obj));
		}
		while(change){
			int minDistI = -1;
			int minDistJ = -1;
			
			double minDistance = Double.MAX_VALUE;
			for(int i=0; i<clusters.size(); i++){
				for(int j=0; j<clusters.size(); j++){
					if(i!=j){
						double distance = distMeasure.getDistance(clusters.get(i), clusters.get(j));
						if(distance<minDistance && distance<limit){
							minDistI = i;
							minDistJ = j;
							minDistance = distance;
						}
					}
				}
			}
			if(minDistI!=-1 && minDistJ!=-1){
				clusters.get(minDistI).mergeWith(clusters.get(minDistJ));
				clusters.remove(minDistJ);
				change=true;
			} else {
				change=false;
			}
			System.out.println("Completed iteration, " + clusters.size() + " clusters");
		}
		for(HierarchicalCluster<ObjectType> cluster : clusters){
			results.add(cluster.getObjects());
		}
		return results;
	}
}

