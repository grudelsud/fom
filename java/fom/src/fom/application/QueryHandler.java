package fom.application;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import fom.model.Cluster;
import fom.model.Post;
import fom.model.Query;
import fom.model.dao.DAOFactory;
import fom.queryexpansion.QueryExpander;
import fom.search.Searcher;

public class QueryHandler {
	
	private DAOFactory daoFactory;
	
	public QueryHandler(DAOFactory daoFactory){
		this.daoFactory=daoFactory;
	}
	
	public void executeQuery(String query, String expEngineName, List<String> sourceNames, DateTime startTime, DateTime endTime){
		
		List<String> expandedQuery = new QueryExpander(expEngineName).expandQuery(query);
		
		List<Post> posts = searchPosts(expandedQuery, startTime, endTime, sourceNames);
		
		List<Cluster> timeClusters = new ArrayList<Cluster>();
		List<Cluster> geoClusters = new ArrayList<Cluster>();
		List<Cluster> semanticClusters = new ArrayList<Cluster>();
		
		timeClusters = timeClustering(posts);
		for(Cluster timeCluster : timeClusters){
			geoClusters.addAll(geoClustering(timeCluster.getPosts()));
			for(Cluster geoCluster : geoClusters){
				semanticClusters.addAll(semanticClustering(geoCluster.getPosts()));
			}
		}
		
		saveData(expandedQuery, sourceNames, startTime, endTime, posts, timeClusters, geoClusters, semanticClusters);
	}
	

	private List<Post> searchPosts(List<String> query, DateTime startTime, DateTime endTime, List<String> sourceNames){
		Searcher searcher = new Searcher();
		for(String sourceName : sourceNames){
			searcher.addSource(sourceName);
		}
		return searcher.search(query, startTime, endTime);
	}
	
	private List<Cluster> timeClustering(List<Post> posts){
		return null;
	}
	
	private List<Cluster> geoClustering(List<Post> posts){
		return null;
	}
	
	private List<Cluster> semanticClustering(List<Post> posts){
		return null;
	}
	
	private void saveData(List<String> expandedQuery, List<String> sourceNames, DateTime startTime, DateTime endTime, List<Post> posts, List<Cluster> timeClusters, List<Cluster> geoClusters, List<Cluster> semanticClusters){
		Query query = new Query();
		daoFactory.getQueryDAO().create(query);
	}
}
