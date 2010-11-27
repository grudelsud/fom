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
import fom.model.Term;
import fom.model.Vocabulary;
import fom.termextraction.YahooTermExtractor;
import fom.utils.StringOperations;

public class SemanticClustering {
	private Clusterer<Post> clusterer;
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
			int k = (int)Math.ceil((double)posts.size()/(double)15);
			AbstractMetric<Post> metric = new TFIDFSimilarity(posts);
			
			clusterer = ClustererFactory.getKMedoidsClusterer(k, metric, 1000);
			List<List<Post>> clusteringResult = clusterer.performClustering(posts);
			for(List<Post> cluster : clusteringResult){
				SemanticCluster currentCluster = new SemanticCluster(originatingQuery);
				String clusterText = new String();
				for(Post post : cluster){
					currentCluster.addPost(post);
					clusterText = clusterText.concat(StringOperations.removeStopwords(post.getContent()) + " ");
				}
				
				List<String> clusterTerms = new YahooTermExtractor().extractKeywords(clusterText);
				for(String term : clusterTerms){
					currentCluster.addTerm(new Term(term, "", null, null, new Vocabulary("MainVoc", "")));
				}
				clusters.add(currentCluster);
			}
		}
		return clusters;
	}
}
