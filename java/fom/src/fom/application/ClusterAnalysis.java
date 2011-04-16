package fom.application;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import fom.clustering.GeoClustering;
import fom.clustering.SemanticKeywordClustering;
import fom.clustering.SemanticTopicClustering;
import fom.model.GeoCluster;
import fom.model.Post;
import fom.model.Query;
import fom.model.KeywordCluster;
import fom.model.TopicCluster;
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
	private int minRTCount;
	private int numberOfTopics;
	private int numberOfWords;
	private int minFollCount;
	private boolean disableLangDetection;
	private boolean excludeRelLinksText;
	private int numberOfKeywords;
	
	public ClusterAnalysis(ResultLogger logger, long userId, DateTime startTime, DateTime endTime, String timeGranularity, String geoGranularity, String sourceName, boolean considerApproxGeolocations, int minRTCount, int numberOfTopics, int numberOfWords, int minFollCount, boolean disableLangDetection, boolean excludeRelLinksText, int numberOfKeywords){
		this.logger = logger;
		this.userId = userId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.timeGranularity = timeGranularity;
		this.geoGranularity = geoGranularity;
		this.sourceName = sourceName;
		this.considerApproxGeolocations = considerApproxGeolocations;
		this.minRTCount = minRTCount;
		this.numberOfTopics = numberOfTopics;
		this.numberOfWords = numberOfWords;
		this.minFollCount = minFollCount;
		this.disableLangDetection = disableLangDetection;
		this.excludeRelLinksText = excludeRelLinksText;
		this.numberOfKeywords = numberOfKeywords;
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
		source.setMinRTCount(minRTCount);
		query.getMeta().put("minRTCount", Integer.toString(minRTCount));
		source.setMinFollCount(minFollCount);
		query.getMeta().put("minFollCount", Integer.toString(minFollCount));
		
		query.getMeta().put("numberOfTopics", Integer.toString(numberOfTopics));
		query.getMeta().put("numberOfWords", Integer.toString(numberOfWords));
		
		query.getMeta().put("numberOfKeywords", Integer.toString(numberOfKeywords));
		
		query.getMeta().put("langDetectionEnabled", Boolean.toString(!disableLangDetection));
		query.getMeta().put("relLinksTextEnabled", Boolean.toString(!excludeRelLinksText));
		
		System.out.println("\tQuery meta: " + query.getMeta().toString());
		
		List<Post> posts = source.searchPosts(terms, query.getStartTime(), query.getEndTime(), query.getLat(), query.getLon(), 0);

		System.out.println("Found " + posts.size() + " posts");
		if(posts.size()==0) return;
		System.out.println("Starting analysis...");
		
		List<GeoCluster> geoClusters = new ArrayList<GeoCluster>();
		List<TopicCluster> semanticClusters = new ArrayList<TopicCluster>();
		
		TimeCluster timeCluster = new TimeCluster(query);
		timeCluster.setStartTime(startTime);
		timeCluster.setEndTime(endTime);
		logger.addTimeCluster(timeCluster);
		query.addCluster(timeCluster);
		geoClusters = new GeoClustering(query, posts, query.getGeoGranularity(), timeCluster).performClustering();
		
		long ldaStartTime = System.currentTimeMillis();
		int geoClusterCount = 0;
		for(GeoCluster geoCluster : geoClusters){
			logger.addGeoCluster(geoCluster);
			System.out.println("Extracting topics from geoCluser " + ++geoClusterCount + " of " + geoClusters.size() + "...");
			query.addCluster(geoCluster);				
			
			//Extract topics
			List<TopicCluster> currentSemanticClusters = new SemanticTopicClustering(query, geoCluster.getPosts(), geoCluster, numberOfTopics, numberOfWords, disableLangDetection, excludeRelLinksText).performClustering();
			semanticClusters.addAll(currentSemanticClusters);
			for(TopicCluster semCluster : currentSemanticClusters){					
				logger.addSemCluster(semCluster);
				query.addCluster(semCluster);
			}
			
			//Extract keywords
			System.out.println("Extracting keywords from geoCluser " + geoClusterCount + " of " + geoClusters.size() + "...");
			KeywordCluster keywordCluster = new SemanticKeywordClustering(query, geoCluster.getPosts(), geoCluster, numberOfKeywords).performClustering();
			query.addCluster(keywordCluster);
			
		}
		System.out.println("Semantic clusters extracted in " + (System.currentTimeMillis()-ldaStartTime)/1000 + "s");
		logger.endLog();
		System.out.println("Logs:\n" + logger.getLogs());
		System.out.println("Saving the results on the DB...");
		long startTime = System.currentTimeMillis();
		DAOFactory.getFactory().getQueryDAO().create(query);
		System.out.println("done in " + (System.currentTimeMillis()-startTime)/1000 + "s");
	}
}
