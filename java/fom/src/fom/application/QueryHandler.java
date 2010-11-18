package fom.application;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import fom.clustering.GeoClustering;
import fom.clustering.SemanticClustering;
import fom.model.Cluster;
import fom.model.Post;
import fom.model.Query;
import fom.model.TimeCluster;
import fom.model.dao.interfaces.DAOFactory;
import fom.queryexpansion.QueryExpander;
import fom.search.Searcher;

public class QueryHandler {
	
	private Query query;
	private String expEngineName;
	private List<String> sourceNames;
	

	public QueryHandler(long userId, String queryString, String expEngineName, List<String> sourceNames, DateTime startTime, DateTime endTime){
		this.expEngineName = expEngineName;
		this.sourceNames = sourceNames;
		query = new Query(userId, queryString, startTime, endTime, "time_gran", 0, 0, "geo_gran", new DateTime(), new DateTime().getZone().getOffset(new DateTime().getMillis())/(1000*60*60));
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
			
			geoClusters.addAll(new GeoClustering(query, timeCluster.getPosts()).performClustering());
			for(Cluster geoCluster : geoClusters){
				query.addCluster(geoCluster);
				
				semanticClusters.addAll(new SemanticClustering(query, geoCluster.getPosts()).performClustering());
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
}
