package fom.search.sources;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import fom.model.Post;
import fom.model.TwitterPost;
import fom.utils.StringOperations;


public class Twitter implements Source {
	
	private twitter4j.Twitter twitter;
	private List<Post> results;
	
	public Twitter(){
		this.results = new ArrayList<Post>();
		twitter = new TwitterFactory().getInstance();
	}

	@Override
	public List<Post> searchPosts(List<String> terms, DateTime startTime, DateTime endTime, double lat, double lon, int radius) {
		try{
			search(terms, startTime, endTime, null, lat, lon, radius);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		return results;
	}

	@Override
	public List<Post> geoSearchPosts(double lat, double lon, int radius, DateTime startTime, DateTime endTime) {
		return new ArrayList<Post>();
	}

	
	private void search(List<String> terms, DateTime since, DateTime until, Long maxId, double lat, double lon, int radius) throws InterruptedException{
		System.setProperty("twitter4j.loggerFactory", "twitter4j.internal.logging.NullLoggerFactory");
		Query query = new Query();
		query.setRpp(100);
		query.setQuery(StringOperations.concatStrings(terms));
		query.setSince(new SimpleDateFormat("yyyy-MM-dd").format(since.toDate().getTime()));
		query.setUntil(new SimpleDateFormat("yyyy-MM-dd").format(until.toDate().getTime()));
		if(lat!=0 || lon!=0) query.setGeoCode(new GeoLocation(lat, lon), radius, "km");
		if(maxId!=null) query.setMaxId(maxId);
		QueryResult result;
		for(int i=1; i<16; i++){
			query.setPage(i);
			try{
				result = twitter.search(query);
		//		System.out.println("Found " + result.getTweets().size() + " tweets");
				System.out.print(".");
				for(Tweet tweet : result.getTweets()){
					saveTweet(tweet);
				}
				if(result.getTweets().size()<100){
					break;
				}
				if(i==15 && result.getTweets().size()==100){
					search(terms, since, until, result.getTweets().get(result.getTweets().size()-1).getId(), lat, lon, radius);
				}
			}catch(TwitterException exc){
				if(exc.getStatusCode()==420){
					System.out.println("Thread put to sleep for " + exc.getRetryAfter() + "seconds");
					Thread.sleep(exc.getRetryAfter()*1000L);
					i--;
				}
			}
		}
	}
	
	private void saveTweet(Tweet tweet) throws TwitterException{

	//	Status status = twitter.showStatus(tweet.getId());
		
	//	System.out.println(status.getPlace());
		
		GeoLocation geoLoc = tweet.getGeoLocation();
		double lat = geoLoc!=null?geoLoc.getLatitude():0;
		double lon = geoLoc!=null?geoLoc.getLongitude():0;
		
		String content = tweet.getText();
		DateTime created = new DateTime(tweet.getCreatedAt());
		int timezone = created.getZone().getOffset(created.getMillis())/(1000*60*60);
		
		results.add(new TwitterPost(0, lat, lon, content, created, created, timezone, null, tweet.getId(), tweet.getFromUserId()));
	}
	
}
