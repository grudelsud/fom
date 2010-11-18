package fom.application;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import fom.model.Cluster;
import fom.model.GeoCluster;
import fom.model.Post;
import fom.model.Query;
import fom.model.SemanticCluster;
import fom.model.TimeCluster;
import fom.model.dao.interfaces.DAOFactory;
import fom.queryexpansion.QueryExpander;
import fom.search.Searcher;

public class QueryHandler {
	
	private Query query;
	private String expEngineName;
	private List<String> sourceNames;
	

	public QueryHandler(String queryString, String expEngineName, List<String> sourceNames, DateTime startTime, DateTime endTime){
		this.expEngineName = expEngineName;
		this.sourceNames = sourceNames;
		query = new Query(0, 1, queryString, startTime, endTime, "time_gran", 0, 0, "geo_gran", new DateTime(), new DateTime().getZone().getOffset(new DateTime().getMillis())/(1000*60*60));
	}
	
	
	public void executeQuery(){
				
		List<String> expandedQuery = new QueryExpander(expEngineName).expandQuery(query.getQuery());
	//	for(String expQueryTerm : expandedQuery){
	//		query.addTerm(new Term(expQueryTerm, "", null, null, null));
	//	}
		
		List<Post> posts = searchPosts(expandedQuery);
		
		List<Cluster> timeClusters = new ArrayList<Cluster>();
		List<Cluster> geoClusters = new ArrayList<Cluster>();
		List<Cluster> semanticClusters = new ArrayList<Cluster>();
		
		timeClusters = timeClustering(posts);
		for(Cluster timeCluster : timeClusters){
			query.addCluster(timeCluster);
			
			geoClusters.addAll(geoClustering(timeCluster.getPosts()));
			for(Cluster geoCluster : geoClusters){
				query.addCluster(geoCluster);
				
				semanticClusters.addAll(semanticClustering(geoCluster.getPosts()));
				for(Cluster semCluster : semanticClusters){
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
	
	private List<Cluster> timeClustering(List<Post> posts){
		List<Cluster> timeClusters = new ArrayList<Cluster>();
		TimeCluster cluster = new TimeCluster(query);
		for(Post post : posts){
			cluster.addPost(post);
		}
		timeClusters.add(cluster);
		return timeClusters;
	}
	
	private List<Cluster> geoClustering(List<Post> posts){
		List<Cluster> geoClusters = new ArrayList<Cluster>();
		GeoCluster cluster = new GeoCluster(query);
		for(Post post : posts){
			cluster.addPost(post);
		}
		geoClusters.add(cluster);
		return geoClusters;
	}
	
	private List<Cluster> semanticClustering(List<Post> posts){
		List<Cluster> semClusters = new ArrayList<Cluster>();
		SemanticCluster cluster = new SemanticCluster(query);
		for(Post post : posts){
			cluster.addPost(post);
		}
		semClusters.add(cluster);
		return semClusters;
	}
}
