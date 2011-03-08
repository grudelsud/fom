package fom.clustering;

import java.util.ArrayList;
import java.util.List;

import fom.model.Cluster;
import fom.model.Post;
import fom.model.Query;
import fom.model.SemanticCluster;
import fom.model.Term;
import fom.model.Vocabulary;
import fom.topicextraction.TopicExtractor;

public class SemanticClustering {
	private Cluster parentCluster;
	private List<Post> posts;
	private List<SemanticCluster> clusters;
	private Query originatingQuery;
	
	public SemanticClustering(Query originatingQuery, List<Post> posts, Cluster parentCluster){
		this.originatingQuery = originatingQuery;
		clusters = new ArrayList<SemanticCluster>();
		this.posts = posts;
		this.parentCluster = parentCluster;
	}
	
	public List<SemanticCluster> performClustering(){
		Vocabulary voc = new Vocabulary("MainVoc", "");
		if(posts.size()>0){
			List<List<String>> topics = TopicExtractor.extractTopics(posts);
			for(List<String> topic : topics){
				SemanticCluster currentCluster = new SemanticCluster(originatingQuery, parentCluster);
				for(String word : topic){
					currentCluster.addTerm(new Term(word, "", null, null, voc));
				}
				clusters.add(currentCluster);
			}
		}
		return clusters;
	}
}
