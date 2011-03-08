package fom.clustering.algorithms.hierarchical;

import java.util.ArrayList;
import java.util.List;

import fom.clustering.algorithms.Clusterer;
import fom.clustering.algorithms.DistanceMeasure;
import fom.model.Post;

public class HAC implements Clusterer {

	double limit;
	DistanceMeasure<HierarchicalPostCluster> distMeasure;
	
	public HAC(DistanceMeasure<HierarchicalPostCluster> distMeasure, double limit){
		this.distMeasure = distMeasure;
		this.limit = limit;
	}
		
	@Override
	public List<List<Post>> performClustering(List<Post> data){
		List<List<Post>> results = new ArrayList<List<Post>>();
		boolean change = true;
		List<HierarchicalPostCluster> clusters = new ArrayList<HierarchicalPostCluster>();
		for(Post obj : data){
			clusters.add(new HierarchicalPostCluster(obj));
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
		for(HierarchicalPostCluster cluster : clusters){
			results.add(cluster.getObjects());
		}
		return results;
	}
}

