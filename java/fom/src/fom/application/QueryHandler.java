package fom.application;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import fom.clustering.GeoClustering;
import fom.clustering.SemanticClustering;
import fom.clustering.TimeClustering;
import fom.model.Cluster;
import fom.model.GeoCluster;
import fom.model.Post;
import fom.model.Query;
import fom.model.SemanticCluster;
import fom.model.TimeCluster;
import fom.model.dao.interfaces.DAOFactory;
import fom.queryexpansion.QueryExpander;
import fom.search.Searcher;
import fom.termextraction.YahooTermExtractor;

public class QueryHandler {
	
	private Query query;
	private String expEngineName;
	private List<String> sourceNames;
	private String timeGranularity;
	

	public QueryHandler(long userId, String queryString, String expEngineName, List<String> sourceNames, DateTime startTime, DateTime endTime, String timeGranularity){
		this.expEngineName = expEngineName;
		this.sourceNames = sourceNames;
		this.timeGranularity = timeGranularity;
		query = new Query(userId, queryString, startTime, endTime, timeGranularity, 0, 0, "geo_gran", new DateTime(), new DateTime().getZone().getOffset(new DateTime().getMillis())/(1000*60*60));
	}
	
	
	public void executeQuery(){
		System.out.println(query.toString());
		
		
		System.out.println("Expanding query...");
		List<String> expandedQuery = new QueryExpander(expEngineName).expandQuery(query.getQuery());
	//	for(String expQueryTerm : expandedQuery){
	//		query.addTerm(new Term(expQueryTerm, "", null, null, null));
	//	}
		
		System.out.println("Searching for posts matching the query " + expandedQuery + "...");
		List<Post> posts = searchPosts(expandedQuery);
		
		List<TimeCluster> timeClusters = new ArrayList<TimeCluster>();
		List<GeoCluster> geoClusters = new ArrayList<GeoCluster>();
		List<SemanticCluster> semanticClusters = new ArrayList<SemanticCluster>();
		
		System.out.println("Clustering...");
		
		timeClusters = new TimeClustering(query, posts, timeGranularity).performClustering();
		for(Cluster timeCluster : timeClusters){
			System.out.println("\n(*)TimeCluster, meta:" + timeCluster.getMeta() + " size:" + timeCluster.getPosts().size());
			query.addCluster(timeCluster);
			
			List<GeoCluster> currentGeoClusters = new GeoClustering(query, timeCluster.getPosts()).performClustering();
			geoClusters.addAll(currentGeoClusters);
			for(Cluster geoCluster : currentGeoClusters){
				System.out.println("\n\t(-)GeoCluster, meta:" + geoCluster.getMeta() + " size:" + geoCluster.getPosts().size());
				query.addCluster(geoCluster);

				List<SemanticCluster> currentSemanticClusters = new SemanticClustering(query, geoCluster.getPosts()).performClustering();
				semanticClusters.addAll(currentSemanticClusters);
				for(Cluster semCluster : currentSemanticClusters){
					System.out.println("\n\t\t(+)SemCluster, meta:" + semCluster.getMeta() + " size:" + semCluster.getPosts().size());
					String clusterText = new String();
					System.out.println("\n\t\t\t ***CLUSTER POSTS:");
					for(Post post: semCluster.getPosts()){
						System.out.println("\t\t\t\t ----> " + post.getContent());
						clusterText = clusterText.concat(post.getContent() + " ");
					}
					List<String> clusterTerms = new YahooTermExtractor().extractKeywords(clusterText);
					System.out.println("\n\t\t\t ***CLUSTER TERMS: " + clusterTerms + "***");
					query.addCluster(semCluster);
				}
			}
		}
		
		DAOFactory.getFactory().getQueryDAO().create(query);
	}
	

	private List<Post> searchPosts(List<String> expandedQuery){
		Searcher searcher = new Searcher();
		for(String sourceName : sourceNames){
			searcher.addSource(sourceName);
		}
		return searcher.search(expandedQuery, query.getStartTime(), query.getEndTime());
	}
}
