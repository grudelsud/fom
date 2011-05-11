package fom.clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fom.langidentification.LanguageIdentifier.Language;
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
			Map<Language, List<Post>> languagePostsMap = splitPostByLanguage(posts);
			for(Language lang : languagePostsMap.keySet()){
				List<Topic> topics = TopicExtractor.extractTopics(languagePostsMap.get(lang), numberOfTopics, numberOfWords, disableLangDetection, excludeRelLinksText, lang);
				for(Topic topic : topics){
					TopicCluster currentCluster = new TopicCluster(originatingQuery, parentCluster, topic.getAlpha(), lang);
					for(String word : topic.getWords()){
						currentCluster.addTerm(new Term(word, "", null, null, voc));
					}
					clusters.add(currentCluster);
				}	
			}
		}
		return clusters;
	}
	
	Map<Language, List<Post>> splitPostByLanguage(List<Post> posts){
		Map<Language, List<Post>> result = new HashMap<Language, List<Post>>();
		
		List<Language> langsToSplit = new ArrayList<Language>();
		langsToSplit.add(Language.italian);
		langsToSplit.add(Language.english);
		langsToSplit.add(Language.french);
		langsToSplit.add(Language.spanish);
		
		for(Post p : posts){
			if(langsToSplit.contains(p.getLanguage())){
				List<Post> splitPosts = result.get(p.getLanguage());
				if(splitPosts==null){
					splitPosts = new ArrayList<Post>();
					splitPosts.add(p);
					result.put(p.getLanguage(), splitPosts);
				} else {
					splitPosts.add(p);
				}	
			} else {
				List<Post> splitPosts = result.get(Language.unknown);
				if(splitPosts==null){
					splitPosts = new ArrayList<Post>();
					splitPosts.add(p);
					result.put(Language.unknown, splitPosts);
				} else {
					splitPosts.add(p);
				}
			}
		}
		
		return result;
	}
	
}
