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
import fom.search.sources.SourceFactory;
import fom.search.sources.SourceFactory.SourceType;

public class ClusterAnalysis implements Runnable{
	
	private ResultLogger logger;
	long userId;
	DateTime startTime;
	DateTime endTime;
	String timeGranularity;
	String geoGranularity;
	
	public ClusterAnalysis(ResultLogger logger, long userId, DateTime startTime, DateTime endTime, String timeGranularity, String geoGranularity){
		this.logger = logger;
		this.userId = userId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.timeGranularity = timeGranularity;
		this.geoGranularity = geoGranularity;
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

		List<Post> posts = SourceFactory.getSource(SourceType.LOCALDB).searchPosts(terms, query.getStartTime(), query.getEndTime(), query.getLat(), query.getLon(), 0);

		System.out.println("Found " + posts.size() + " posts");
		if(posts.size()==0) return;
		System.out.println("Starting analysis...");
		
		List<GeoCluster> geoClusters = new ArrayList<GeoCluster>();
		List<SemanticCluster> semanticClusters = new ArrayList<SemanticCluster>();
		
		TimeCluster timeCluster = new TimeCluster(query);
		timeCluster.getPosts().addAll(posts);
		logger.addTimeCluster(timeCluster);
		query.addCluster(timeCluster);
		List<GeoCluster> currentGeoClusters = new GeoClustering(query, timeCluster.getPosts(), query.getGeoGranularity(), timeCluster).performClustering();
		geoClusters.addAll(currentGeoClusters);
		for(GeoCluster geoCluster : currentGeoClusters){
			logger.addGeoCluster(geoCluster);
			query.addCluster(geoCluster);
			
			List<SemanticCluster> currentSemanticClusters = new SemanticClustering(query, geoCluster.getPosts(), geoCluster).performClustering();
			semanticClusters.addAll(currentSemanticClusters);
			for(SemanticCluster semCluster : currentSemanticClusters){					
				logger.addSemCluster(semCluster);
				query.addCluster(semCluster);
			}
		}
		logger.endLog();
		System.out.println("Logs:\n" + logger.getLogs());
		DAOFactory.getFactory().getQueryDAO().create(query);
	}
}
