package fom.clustering.algorithms.hierarchical;

import java.util.ArrayList;
import java.util.List;

import fom.clustering.algorithms.Clusterer;
import fom.clustering.algorithms.DistanceMeasure;
import fom.model.Post;

public class GreedyHAC implements Clusterer {

	double limit;
	DistanceMeasure<HierarchicalPostCluster> distMeasure;
	
	public GreedyHAC(DistanceMeasure<HierarchicalPostCluster> distMeasure, double limit){
		this.distMeasure = distMeasure;
		this.limit = limit;
	}
		
	@Override
	public List<List<Post>> performClustering(List<Post> data){
		long startTime = System.currentTimeMillis();
		List<List<Post>> results = new ArrayList<List<Post>>();
		boolean change = true;
		List<HierarchicalPostCluster> clusters = new ArrayList<HierarchicalPostCluster>(data.size());
		for(Post obj : data){
			clusters.add(new HierarchicalPostCluster(obj));
		}
		while(change){
			System.out.println("Starting iteration...");
			long iterStartTime = System.currentTimeMillis();
			change=false;
			for(int i=0; i<clusters.size(); i++){
				for(int j=0; j<clusters.size(); j++){
					if(i!=j){
						double distance = distMeasure.getDistance(clusters.get(i), clusters.get(j));
						if(distance<limit){
							clusters.get(i).mergeWith(clusters.get(j));
							clusters.remove(j);
							change=true;
							break;
						}						
					}
				}
			}
			System.out.println("Iteration ended in " + (System.currentTimeMillis()-iterStartTime)/1000 + " sec, " + clusters.size() + " clusters found");
		}
		for(HierarchicalPostCluster cluster : clusters){
			results.add(cluster.getObjects());
		}
		System.out.println("Clustering completed in " + (System.currentTimeMillis()-startTime)/1000 + " sec");
		return results;
	}
}

