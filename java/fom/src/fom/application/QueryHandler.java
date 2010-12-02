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
import fom.resultlogging.logengines.CSVLogger;
import fom.resultlogging.logengines.ConsoleLogger;
import fom.resultlogging.logengines.FolderLogger;
import fom.resultlogging.logengines.RPCRemoteLogger;
import fom.search.Searcher;

public class QueryHandler {
	
	private Query query;
	private String expEngineName;
	private List<String> sourceNames;
	private int radius;
	

	public QueryHandler(long userId, String queryString, String expEngineName, List<String> sourceNames, DateTime startTime, DateTime endTime, String timeGranularity, String geoGranularity, double nearLat, double nearLon, int radius){
		this.expEngineName = expEngineName;
		this.sourceNames = sourceNames;
		this.radius = radius;
		query = new Query(userId, queryString, startTime, endTime, timeGranularity, nearLat, nearLon, geoGranularity, new DateTime(), new DateTime().getZone().getOffset(new DateTime().getMillis())/(1000*60*60));
	}
	
	
	public void executeQuery(){
		List<String> expandedQuery = new QueryExpander(expEngineName).expandQuery(query.getQuery());
		for(String expQueryTerm : expandedQuery){
			query.addTerm(new Term(expQueryTerm, "", null, null, new Vocabulary("MainVoc", "")));
		}

		ResultLogger logger = new ResultLogger();
		logger.addLogEngine(new CSVLogger());
	//	logger.addLogEngine(new RPCRemoteLogger());
		logger.addLogEngine(new ConsoleLogger());
		logger.addLogEngine(new FolderLogger());
		logger.startLogging(query);
		
		List<Post> posts = searchPosts(expandedQuery.subList(0, 20));

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
	

	private List<Post> searchPosts(List<String> expandedQuery){
		Searcher searcher = new Searcher();
		for(String sourceName : sourceNames){
			searcher.addSource(sourceName);
		}
		return searcher.search(expandedQuery, query.getStartTime(), query.getEndTime(), query.getLat(), query.getLon(), radius);
	}
}
