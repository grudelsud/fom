package fom.clustering;

import java.util.ArrayList;
import java.util.List;

import fom.clustering.algorithms.Clusterer;
import fom.clustering.algorithms.ClustererFactory;
import fom.clustering.algorithms.kmedoids.metrics.AbstractMetric;
import fom.clustering.algorithms.kmedoids.metrics.TFIDFSimilarity;
import fom.model.Post;
import fom.model.Query;
import fom.model.SemanticCluster;

public class SemanticClustering {
	private Clusterer<String> clusterer;
	private List<Post> posts;
	private List<SemanticCluster> clusters;
	private Query originatingQuery;
	
	public SemanticClustering(Query originatingQuery, List<Post> posts){
		this.originatingQuery = originatingQuery;
		clusters = new ArrayList<SemanticCluster>();
		this.posts = posts;
	}
	
	public List<SemanticCluster> performClustering(){
		
		if(posts.size()>0){
			String[] objects = new String[posts.size()];
			for(int i=0; i<posts.size(); i++){
				objects[i] = posts.get(i).getContent();
			}
			int k = (int)Math.ceil((double)posts.size()/(double)15);
			AbstractMetric<String> metric = new TFIDFSimilarity();
			
			clusterer = ClustererFactory.getKMedoidsClusterer(objects, k, metric, 1000);
			clusterer.performClustering();
			for(int clusterIndex=0; clusterIndex<k; clusterIndex++){
				SemanticCluster currentCluster = new SemanticCluster(originatingQuery);
				for(int postIndex=0; postIndex<posts.size(); postIndex++){
					if(clusterer.getClusterIndexes()[postIndex]==clusterIndex){
						currentCluster.addPost(posts.get(postIndex));
					}
				}
				clusters.add(currentCluster);
			}		
		}
		return clusters;
	}
}
