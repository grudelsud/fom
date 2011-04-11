package fom.application;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import fom.clustering.GeoClustering;
import fom.clustering.SemanticClustering;
import fom.model.GeoCluster;
import fom.model.Link;
import fom.model.Post;
import fom.model.Query;
import fom.model.RelatedLinksCluster;
import fom.model.SemanticCluster;
import fom.model.Term;
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
	
	public ClusterAnalysis(ResultLogger logger, long userId, DateTime startTime, DateTime endTime, String timeGranularity, String geoGranularity, String sourceName, boolean considerApproxGeolocations, int minRTCount, int numberOfTopics, int numberOfWords, int minFollCount, boolean disableLangDetection, boolean excludeRelLinksText){
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
		
		query.getMeta().put("langDetectionEnabled", Boolean.toString(!disableLangDetection));
		query.getMeta().put("relLinksTextEnabled", Boolean.toString(!excludeRelLinksText));
		
		System.out.println("Query meta: " + query.getMeta().toString());
		
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
		int geoClusterCount = 0;
		for(GeoCluster geoCluster : geoClusters){
			logger.addGeoCluster(geoCluster);
			System.out.println("Extracting topics from geoCluser " + ++geoClusterCount + " of " + geoClusters.size() + "...");
			query.addCluster(geoCluster);				
			
			List<SemanticCluster> currentSemanticClusters = new SemanticClustering(query, geoCluster.getPosts(), geoCluster, numberOfTopics, numberOfWords, disableLangDetection, excludeRelLinksText).performClustering();
			semanticClusters.addAll(currentSemanticClusters);
			for(SemanticCluster semCluster : currentSemanticClusters){					
				logger.addSemCluster(semCluster);
				query.addCluster(semCluster);
			}
			
			for(Post p : geoCluster.getPosts()){
				if(p.getLinks().size()!=0){
					for(Link l : p.getLinks()){
						if(l.getMeta().get("keywords")!=null){
							RelatedLinksCluster relClust = new RelatedLinksCluster(query, geoCluster);
							relClust.addPost(p);
							String[] keyws = l.getMeta().get("keywords").split("[,;!]");
							for(String keyw : keyws){
								relClust.addTerm(new Term(keyw, "", null, null, null));
							}
							query.addCluster(relClust);
						}
					}
				}
			}
		}
		System.out.println("Topics extracted in " + (System.currentTimeMillis()-ldaStartTime)/1000 + "s");
		logger.endLog();
		System.out.println("Logs:\n" + logger.getLogs());
		System.out.println("Saving the results on the DB...");
		long startTime = System.currentTimeMillis();
		DAOFactory.getFactory().getQueryDAO().create(query);
		System.out.println("done in " + (System.currentTimeMillis()-startTime)/1000 + "s");
	}
}
