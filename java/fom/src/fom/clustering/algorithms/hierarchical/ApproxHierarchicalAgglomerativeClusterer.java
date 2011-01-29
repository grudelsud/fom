package fom.clustering.algorithms.hierarchical;

import java.util.ArrayList;
import java.util.List;

import fom.clustering.algorithms.Clusterer;
import fom.clustering.algorithms.DistanceMeasure;

public class ApproxHierarchicalAgglomerativeClusterer<ObjectType> implements Clusterer<ObjectType> {

	double limit;
	DistanceMeasure<HierarchicalCluster<ObjectType>> distMeasure;
	
	public ApproxHierarchicalAgglomerativeClusterer(DistanceMeasure<HierarchicalCluster<ObjectType>> distMeasure, double limit){
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
			change=false;
			for(int i=0; i<clusters.size(); i++){
				for(int j=i+1; j<clusters.size(); j++){
					double distance = distMeasure.getDistance(clusters.get(i), clusters.get(j));
					if(distance<limit){
						clusters.get(i).mergeWith(clusters.get(j));
						clusters.remove(j);
						change=true;
					}
				}
			}
		}
		for(HierarchicalCluster<ObjectType> cluster : clusters){
			results.add(cluster.getObjects());
		}
		return results;
	}
}

