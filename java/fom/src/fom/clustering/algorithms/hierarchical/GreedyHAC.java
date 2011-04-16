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
		
		List<HierarchicalPostCluster> clusters = new ArrayList<HierarchicalPostCluster>(data.size());
		for(Post obj : data){
			clusters.add(new HierarchicalPostCluster(obj));
		}
		
		boolean didMerge = true;
		List<Integer> toBeRemoved = new ArrayList<Integer>();
		
		while(didMerge){
			didMerge = false;
			int lastIMerged = 0;
			for(int i=0; i<clusters.size(); i++){
				HierarchicalPostCluster clusterI = clusters.get(i);
				for(int j=0; j<clusters.size(); j++){
					if(i!=j && distMeasure.getDistance(clusterI, clusters.get(j))<limit){
						clusterI.mergeWith(clusters.get(j));
						toBeRemoved.add(j);
						didMerge = true;
					}
				}
				if(didMerge){
					int removedCount = 0;
					for(Integer j : toBeRemoved){
						clusters.remove(j.intValue()-removedCount++);
					}
					toBeRemoved.clear();
					
					if(lastIMerged!=i){
						System.out.println("Merging posts: " + clusters.size() + " clusters found");
					}
					break;
				}
			}
		}
				
		for(HierarchicalPostCluster cluster : clusters){
			results.add(cluster.getObjects());
		}
		System.out.println("Clustering completed in " + (System.currentTimeMillis()-startTime)/1000 + " sec, found " + clusters.size() + " clusters");
		return results;
	}
}

