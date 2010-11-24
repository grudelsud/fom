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
import fom.resultlogging.logengines.RPCRemoteLogger;
import fom.search.Searcher;

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
		for(String expQueryTerm : expandedQuery){
			query.addTerm(new Term(expQueryTerm, "", null, null, new Vocabulary("MainVoc", "")));
		}
		
		List<Post> posts = searchPosts(expandedQuery);
		
		ResultLogger logger = new ResultLogger();
		logger.addLogEngine(new CSVLogger());
		logger.addLogEngine(new RPCRemoteLogger());
		
		logger.startLogging(query);
		
		System.out.println("Clustering...");
		List<TimeCluster> timeClusters = new ArrayList<TimeCluster>();
		List<GeoCluster> geoClusters = new ArrayList<GeoCluster>();
		List<SemanticCluster> semanticClusters = new ArrayList<SemanticCluster>();
		
		timeClusters = new TimeClustering(query, posts, timeGranularity).performClustering();
		for(TimeCluster timeCluster : timeClusters){
			
			logger.addTimeCluster(timeCluster);

			query.addCluster(timeCluster);
			
			List<GeoCluster> currentGeoClusters = new GeoClustering(query, timeCluster.getPosts()).performClustering();
			geoClusters.addAll(currentGeoClusters);
			for(GeoCluster geoCluster : currentGeoClusters){
				logger.addGeoCluster(geoCluster);
				
				query.addCluster(geoCluster);
				
				List<SemanticCluster> currentSemanticClusters = new SemanticClustering(query, geoCluster.getPosts()).performClustering();
				semanticClusters.addAll(currentSemanticClusters);
				for(SemanticCluster semCluster : currentSemanticClusters){					
					logger.addSemCluster(semCluster);
					
					query.addCluster(semCluster);
					System.out.print(".");
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
		return searcher.search(expandedQuery, query.getStartTime(), query.getEndTime());
	}
}
