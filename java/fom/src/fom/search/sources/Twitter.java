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
import fom.utils.StringOperations;


public class Twitter implements GeoCapableSource {
	
	private List<Post> results;
	
	public Twitter(){
		this.results = new ArrayList<Post>();
	}

	@Override
	public List<Post> searchPosts(List<String> terms, DateTime startTime, DateTime endTime) {
		try{
			search(terms, startTime, endTime, null);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		return results;
	}

	@Override
	public List<Post> geoSearchPosts(float lat, float lon, int radius, DateTime startTime, DateTime endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	
	private void search(List<String> terms, DateTime since, DateTime until, Long maxId) throws InterruptedException{
		twitter4j.Twitter twitter = new TwitterFactory().getInstance();
		Query query = new Query();
		query.setRpp(100);
		query.setQuery(StringOperations.concatStrings(terms));
		query.setSince(new SimpleDateFormat("yyyy-MM-dd").format(since.toDate().getTime()));
		query.setUntil(new SimpleDateFormat("yyyy-MM-dd").format(until.toDate().getTime()));
		if(maxId!=null) query.setMaxId(maxId);
		QueryResult result;
		for(int i=1; i<16; i++){
			query.setPage(i);
			try{
				result = twitter.search(query);
				System.out.println("Found " + result.getTweets().size() + " tweets");
				for(Tweet tweet : result.getTweets()){
					saveTweet(tweet);
				}
				if(result.getTweets().size()<100){
					break;
				}
				if(i==15 && result.getTweets().size()==100){
					search(terms, since, until, result.getTweets().get(result.getTweets().size()-1).getId());
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
	
	private void saveTweet(Tweet tweet){
		GeoLocation geoLoc = tweet.getGeoLocation();
		double lat = geoLoc!=null?geoLoc.getLatitude():0;
		double lon = geoLoc!=null?geoLoc.getLongitude():0;
		
		String content = tweet.getText();
		DateTime created = new DateTime(tweet.getCreatedAt());
		
		results.add(new Post(lat, lon, content, created));
	}
	
}
