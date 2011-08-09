package fom.clustering;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import fom.model.Cluster;
import fom.model.KeywordCluster;
import fom.model.Link;
import fom.model.Post;
import fom.model.Query;
import fom.model.Term;
import fom.utils.StringOperations;

public class SemanticKeywordClustering {
	private Cluster parentCluster;
	private List<Post> posts;
	private Query originatingQuery;
	private int numberOfKeywords;
	
	public SemanticKeywordClustering(Query originatingQuery, List<Post> posts, Cluster parentCluster, int numberOfKeywords){
		this.originatingQuery = originatingQuery;
		this.posts = posts;
		this.parentCluster = parentCluster;
		this.numberOfKeywords = numberOfKeywords;
	}
	
	public KeywordCluster performClustering(){
		KeywordCluster keywordClust = new KeywordCluster(originatingQuery, parentCluster);
		Map<String, Integer> keywords = new HashMap<String, Integer>();
		int kwTot = 0;
		for(Post p : posts){
			//Extract the hashtags:
			List<String> hashtags = StringOperations.extractHashtags(p.getContent());
			for(String hashtag : hashtags){
				hashtag = StringOperations.removeHash(hashtag);
				Integer htagCount = keywords.get(hashtag.toLowerCase());
				kwTot++;
				if(htagCount==null){
					keywords.put(hashtag.toLowerCase(), 1);
				} else {
					keywords.put(hashtag.toLowerCase(), htagCount+1);
				}
			}
			
			//Extract the keywords from the link meta:
			for(Link l : p.getLinks()){
				if(l.getMeta().get("keywords")!=null){
					String[] keyws = l.getMeta().get("keywords").split("[,;!]");
					for(String keyw : keyws){
						Integer keywCount = keywords.get(keyw.toLowerCase());
						kwTot++;
						if(keywCount==null){
							keywords.put(keyw.toLowerCase(), 1);
						} else {
							keywords.put(keyw.toLowerCase(), keywCount+1);
						}
					}
				}
			}
		}
		
		PriorityQueue<Keyword> keywordQueue = new PriorityQueue<Keyword>();
		
		for(Entry<String, Integer> k : keywords.entrySet()){
			keywordQueue.add(new Keyword(k.getKey(), k.getValue()));
		}
		
		int limit = keywordQueue.size() > numberOfKeywords ? numberOfKeywords : keywordQueue.size();
		for(int i=0; i<limit; i++){
			Keyword kw = keywordQueue.remove();
	//		System.out.println("Added term " + kw.keyword + " with frequency of " + kw.occurrences );
			keywordClust.addTerm(new Term(kw.keyword, (float)kw.occurrences / (float)kwTot, "", null, null, null));
		}
		
	//	System.out.println(keywords);
		return keywordClust;
	}
	
	private class Keyword implements Comparable<Keyword>{
		String keyword;
		int occurrences;
		
		public Keyword(String keyword, int occurrences) {
			this.keyword = keyword;
			this.occurrences = occurrences;
		}

		@Override
		public int compareTo(Keyword arg0) {
			if(arg0.occurrences<this.occurrences){
				return -1;
			} else if(arg0.occurrences>this.occurrences){
				return 1;
			} else {
				return 0;
			}
		}
		
		@Override
		public boolean equals(Object arg0) {
			if(arg0.getClass()!=Keyword.class){
				return false;
			} else {
				Keyword k = (Keyword) arg0;
				return k.keyword.equalsIgnoreCase(keyword);
			}
		}
	}
}
