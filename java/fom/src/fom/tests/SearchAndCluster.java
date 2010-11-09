package fom.tests;

import java.util.ArrayList;

import org.joda.time.DateTime;

import fom.clustering.algorithms.Clusterer;
import fom.clustering.algorithms.ClustererFactory;
import fom.clustering.algorithms.kmedoids.metrics.TFIDFSimilarity;
import fom.model.Post;
import fom.search.Searcher;

public class SearchAndCluster {

	public static void main(String[] args){
		ArrayList<String> terms = new ArrayList<String>();
		terms.add("obama");
		terms.add("vote");
		
		Searcher searcher = new Searcher();
		searcher.addSource("Twitter");
		
		DateTime startTime = new DateTime().minusDays(1);
		DateTime endTime = new DateTime();
		
		ArrayList<String> postContents = new ArrayList<String>();
		for(Post post: searcher.search(terms, startTime, endTime)){
			postContents.add(post.getContent());
		}
		
		Clusterer<String> clusterer = new ClustererFactory<String>().kMedoidsClusterer(postContents.toArray(new String[0]), postContents.size()/15, new  TFIDFSimilarity() , 1000);
		
		for(int clusterIndex=0; clusterIndex<postContents.size()/15; clusterIndex++){
			System.out.println("*** CLUSTER " + clusterIndex + " ***");
			for(int postIndex=0; postIndex<postContents.size(); postIndex++){
				if(clusterer.getClusterIndexes()[postIndex]==clusterIndex){
					System.out.println("\t-) " + postContents.get(postIndex));
				}
			}
			System.out.println("****************\n");
		}
	}
	
}
