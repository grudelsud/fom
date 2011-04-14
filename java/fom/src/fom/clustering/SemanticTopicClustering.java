package fom.clustering;

import java.util.ArrayList;
import java.util.List;

import fom.model.Cluster;
import fom.model.Post;
import fom.model.Query;
import fom.model.TopicCluster;
import fom.model.Term;
import fom.model.Topic;
import fom.model.Vocabulary;
import fom.topicextraction.TopicExtractor;

public class SemanticTopicClustering {
	private Cluster parentCluster;
	private List<Post> posts;
	private List<TopicCluster> clusters;
	private Query originatingQuery;
	private int numberOfTopics;
	private int numberOfWords;
	private boolean disableLangDetection;
	private boolean excludeRelLinksText;
	
	public SemanticTopicClustering(Query originatingQuery, List<Post> posts, Cluster parentCluster, int numberOfTopics, int numberOfWords, boolean disableLangDetection, boolean excludeRelLinksText){
		this.originatingQuery = originatingQuery;
		clusters = new ArrayList<TopicCluster>();
		this.posts = posts;
		this.parentCluster = parentCluster;
		this.numberOfTopics = numberOfTopics;
		this.numberOfWords = numberOfWords;
		this.disableLangDetection = disableLangDetection;
		this.excludeRelLinksText = excludeRelLinksText;
	}
	
	public List<TopicCluster> performClustering(){
		Vocabulary voc = new Vocabulary("MainVoc", "");
		if(posts.size()>0){
			List<Topic> topics = TopicExtractor.extractTopics(posts, numberOfTopics, numberOfWords, disableLangDetection, excludeRelLinksText);
			for(Topic topic : topics){
				TopicCluster currentCluster = new TopicCluster(originatingQuery, parentCluster, topic.getAlpha());
				for(String word : topic.getWords()){
					currentCluster.addTerm(new Term(word, "", null, null, voc));
				}
				clusters.add(currentCluster);
			}
		}
		return clusters;
	}
}
