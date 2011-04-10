package fom.clustering;

import java.util.ArrayList;
import java.util.List;

import fom.model.Cluster;
import fom.model.Post;
import fom.model.Query;
import fom.model.SemanticCluster;
import fom.model.Term;
import fom.model.Topic;
import fom.model.Vocabulary;
import fom.topicextraction.TopicExtractor;

public class SemanticClustering {
	private Cluster parentCluster;
	private List<Post> posts;
	private List<SemanticCluster> clusters;
	private Query originatingQuery;
	private int numberOfTopics;
	private int numberOfWords;
	
	public SemanticClustering(Query originatingQuery, List<Post> posts, Cluster parentCluster, int numberOfTopics, int numberOfWords){
		this.originatingQuery = originatingQuery;
		clusters = new ArrayList<SemanticCluster>();
		this.posts = posts;
		this.parentCluster = parentCluster;
		this.numberOfTopics = numberOfTopics;
		this.numberOfWords = numberOfWords;
	}
	
	public List<SemanticCluster> performClustering(){
		Vocabulary voc = new Vocabulary("MainVoc", "");
		if(posts.size()>0){
			List<Topic> topics = TopicExtractor.extractTopics(posts, numberOfTopics, numberOfWords);
			for(Topic topic : topics){
				SemanticCluster currentCluster = new SemanticCluster(originatingQuery, parentCluster, topic.getAlpha());
				for(String word : topic.getWords()){
					currentCluster.addTerm(new Term(word, "", null, null, voc));
				}
				clusters.add(currentCluster);
			}
		}
		return clusters;
	}
}
