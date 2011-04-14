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
		boolean shouldMerge = true;
		
		List<HierarchicalPostCluster> clusters = new ArrayList<HierarchicalPostCluster>(data.size());
		for(Post obj : data){
			clusters.add(new HierarchicalPostCluster(obj));
		}
		

		int lastFirstIndexMerged = 0;
		
		while(shouldMerge){
			shouldMerge=false;
			int toBeMergedFirstIndex = 0;
			int toBeMergedSecondIndex = 0;
			
			for(int i=0; i<clusters.size(); i++){
				for(int j=i+1; j<clusters.size(); j++){
					double distance = distMeasure.getDistance(clusters.get(i), clusters.get(j));
					if(distance<limit){
						shouldMerge=true;
						toBeMergedFirstIndex = i;
						toBeMergedSecondIndex = j;
						break;
					}						
				}
				if(shouldMerge){
					break;
				}
			}
			if(shouldMerge){
				clusters.get(toBeMergedFirstIndex).mergeWith(clusters.get(toBeMergedSecondIndex));
				clusters.remove(toBeMergedSecondIndex);
				if(toBeMergedFirstIndex!=lastFirstIndexMerged){
					System.out.println(((float)toBeMergedFirstIndex/(float)clusters.size())*100 + "%");
					lastFirstIndexMerged = toBeMergedFirstIndex;
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

