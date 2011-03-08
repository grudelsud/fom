package fom.application;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import fom.clustering.GeoClustering;
import fom.clustering.SemanticClustering;
import fom.model.GeoCluster;
import fom.model.Post;
import fom.model.Query;
import fom.model.SemanticCluster;
import fom.model.TimeCluster;
import fom.model.dao.interfaces.DAOFactory;
import fom.resultlogging.ResultLogger;
import fom.search.sources.LocalDBSource;

public class ClusterAnalysis implements Runnable{
	
	private ResultLogger logger;
	private long userId;
	private DateTime startTime;
	private DateTime endTime;
	private String timeGranularity;
	private String geoGranularity;
	private String sourceName;
	private boolean considerApproxGeolocations;
	
	public ClusterAnalysis(ResultLogger logger, long userId, DateTime startTime, DateTime endTime, String timeGranularity, String geoGranularity, String sourceName, boolean considerApproxGeolocations){
		this.logger = logger;
		this.userId = userId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.timeGranularity = timeGranularity;
		this.geoGranularity = geoGranularity;
		this.sourceName = sourceName;
		this.considerApproxGeolocations = considerApproxGeolocations;
	}

	@Override
	public void run() {
		Query query = new Query(userId, "", startTime, endTime, timeGranularity, 0, 0, 0, geoGranularity, new DateTime(), new DateTime().getZone().getOffset(new DateTime().getMillis())/(1000*60*60));
		
		logger.startLogging(query);
		
		List<String> terms = new ArrayList<String>();
		
		System.out.println("Gathering posts from db:");
		System.out.println("\tAnalysis time span: " + timeGranularity);
		System.out.println("\tGeo granularity: " + geoGranularity);
		System.out.println("\tStart time: " + startTime);
		System.out.println("\tEnd time: " + endTime);

		LocalDBSource source = new LocalDBSource();
		source.setSourceName(sourceName);
		query.getMeta().put("sourceName", sourceName);
		source.setConsiderApproxGeolocations(considerApproxGeolocations);
		query.getMeta().put("considerApproxGeolocations", Boolean.toString(considerApproxGeolocations));
		
		
		List<Post> posts = source.searchPosts(terms, query.getStartTime(), query.getEndTime(), query.getLat(), query.getLon(), 0);

		System.out.println("Found " + posts.size() + " posts");
		if(posts.size()==0) return;
		System.out.println("Starting analysis...");
		
		List<GeoCluster> geoClusters = new ArrayList<GeoCluster>();
		List<SemanticCluster> semanticClusters = new ArrayList<SemanticCluster>();
		
		TimeCluster timeCluster = new TimeCluster(query);
		timeCluster.setStartTime(startTime);
		timeCluster.setEndTime(endTime);
		logger.addTimeCluster(timeCluster);
		query.addCluster(timeCluster);
		geoClusters = new GeoClustering(query, posts, query.getGeoGranularity(), timeCluster).performClustering();
		
		long ldaStartTime = System.currentTimeMillis();
		System.out.println("Extracting topics...");
		for(GeoCluster geoCluster : geoClusters){
			logger.addGeoCluster(geoCluster);
			query.addCluster(geoCluster);
			
			List<SemanticCluster> currentSemanticClusters = new SemanticClustering(query, geoCluster.getPosts(), geoCluster).performClustering();
			semanticClusters.addAll(currentSemanticClusters);
			for(SemanticCluster semCluster : currentSemanticClusters){					
				logger.addSemCluster(semCluster);
				query.addCluster(semCluster);
			}
		}
		System.out.println("Topic extracted in " + (System.currentTimeMillis()-ldaStartTime)/1000 + " ms");
		logger.endLog();
		System.out.println("Logs:\n" + logger.getLogs());
		System.out.println("Saving results on the DB...");
		long startTime = System.currentTimeMillis();
		DAOFactory.getFactory().getQueryDAO().create(query);
		System.out.println("done in " + (System.currentTimeMillis()-startTime)/1000 + "s");
	}
}
