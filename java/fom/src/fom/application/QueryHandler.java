package fom.application;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import fom.clustering.GeoClustering;
import fom.clustering.SemanticClustering;
import fom.clustering.TimeClustering;
import fom.model.GeoCluster;
import fom.model.Post;
import fom.model.Query;
import fom.model.SemanticCluster;
import fom.model.Term;
import fom.model.TimeCluster;
import fom.model.Vocabulary;
import fom.model.dao.interfaces.DAOFactory;
import fom.queryexpansion.QueryExpander;
import fom.resultlogging.ResultLogger;
import fom.search.Searcher;

public class QueryHandler {
	
	private QueryExpander queryExpander;
	private Searcher searcher;
	private ResultLogger logger;
	
	public QueryHandler(String expEngineName, List<String> sourceNames, ResultLogger logger){
		this.queryExpander = new QueryExpander(expEngineName);
		this.searcher = new Searcher();
		for(String source : sourceNames){
			searcher.addSource(source);
		}
		this.logger = logger;
	}
	
	
	public void executeQuery(long userId, String queryString, DateTime startTime, DateTime endTime, String timeGranularity, String geoGranularity, double nearLat, double nearLon, int radius){
		Query query = new Query(userId, queryString, startTime, endTime, timeGranularity, nearLat, nearLon, geoGranularity, new DateTime(), new DateTime().getZone().getOffset(new DateTime().getMillis())/(1000*60*60));
		List<String> expandedQuery = queryExpander.expandQuery(query.getQuery());
		for(String expQueryTerm : expandedQuery){
			query.addTerm(new Term(expQueryTerm, "", null, null, new Vocabulary("MainVoc", "")));
		}
		
		logger.startLogging(query);
		
		List<Post> posts = searcher.search(expandedQuery.subList(0, expandedQuery.size()>20?20:expandedQuery.size()), query.getStartTime(), query.getEndTime(), query.getLat(), query.getLon(), radius);

		if(posts.size()==0) return;
		
		List<TimeCluster> timeClusters = new ArrayList<TimeCluster>();
		List<GeoCluster> geoClusters = new ArrayList<GeoCluster>();
		List<SemanticCluster> semanticClusters = new ArrayList<SemanticCluster>();
		
		timeClusters = new TimeClustering(query, posts, query.getTimeGranularity()).performClustering();
		for(TimeCluster timeCluster : timeClusters){
			logger.addTimeCluster(timeCluster);
			query.addCluster(timeCluster);

			List<GeoCluster> currentGeoClusters = new GeoClustering(query, timeCluster.getPosts(), query.getGeoGranularity()).performClustering();
			geoClusters.addAll(currentGeoClusters);
			for(GeoCluster geoCluster : currentGeoClusters){
				logger.addGeoCluster(geoCluster);
				query.addCluster(geoCluster);
				
				List<SemanticCluster> currentSemanticClusters = new SemanticClustering(query, geoCluster.getPosts()).performClustering();
				semanticClusters.addAll(currentSemanticClusters);
				for(SemanticCluster semCluster : currentSemanticClusters){					
					logger.addSemCluster(semCluster);
					query.addCluster(semCluster);
				}
			}
		}
		logger.endLog();
		System.out.println("Logs:\n" + logger.getLogs());
		DAOFactory.getFactory().getQueryDAO().create(query);
	}
}
