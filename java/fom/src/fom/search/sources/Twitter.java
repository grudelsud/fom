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

import fom.geocoding.Geocoder;
import fom.geocoding.LocalGeonamesGeocoder;
import fom.langidentification.LanguageIdentifier;
import fom.langidentification.LanguageIdentifier.Language;
import fom.langidentification.Lc4jLangIdentifier;
import fom.model.Post;
import fom.model.TwitterPost;
import fom.utils.StringOperations;


public class Twitter implements Source {
	
	private twitter4j.Twitter twitter;
	private List<Post> results;
	private LanguageIdentifier langIdentifier;
	
	public Twitter(){
		this.results = new ArrayList<Post>();
		twitter = new TwitterFactory().getInstance();
		this.langIdentifier = new Lc4jLangIdentifier();
	}

	@Override
	public List<Post> searchPosts(List<String> terms, DateTime startTime, DateTime endTime, double lat, double lon, int radius) {
		try{
			int numberOfTermsPerSearch = 10;
			int numberOfChunks = (int) Math.ceil(((double)terms.size())/((double)numberOfTermsPerSearch));
			for(int i=0; i<numberOfChunks; i++){
				search(terms.subList(i*numberOfChunks, (i+1)*numberOfChunks<=terms.size()?(i+1)*numberOfChunks:terms.size()), startTime, endTime, null, lat, lon, radius);				
			}
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		return results;
	}

	
	private void search(List<String> terms, DateTime since, DateTime until, Long maxId, double lat, double lon, int radius) throws InterruptedException{
		System.setProperty("twitter4j.loggerFactory", "twitter4j.internal.logging.NullLoggerFactory");
		Query query = new Query();
		query.setRpp(100);
		String queryString = StringOperations.logicOrConcatStrings(terms);
		if(!queryString.equalsIgnoreCase("")) query.setQuery(queryString);
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
				} else {
					exc.printStackTrace();			
				}
			}
		}
	}
	
	private void saveTweet(Tweet tweet) throws TwitterException{

	//	Status status = twitter.showStatus(tweet.getId());
		
	//	System.out.println(status.getPlace());
		
		double lat = 0;
		double lon = 0;
		boolean coordinatesEstimated = false;
		
		GeoLocation geoLoc = tweet.getGeoLocation();
		if(geoLoc != null){
			lat = geoLoc.getLatitude();
			lon = geoLoc.getLongitude();
		} else {
			if(tweet.getLocation()!=null){
				Geocoder geocoder = new LocalGeonamesGeocoder();
				double[] coords = geocoder.geocode(tweet.getLocation());
				if(coords[0]!=0 || coords[1]!=0){
					lat = coords[0];
					lon = coords[1];
					coordinatesEstimated = true;
				}
			}
		}
		
		String content = tweet.getText();
		DateTime created = new DateTime(tweet.getCreatedAt());
		int timezone = created.getZone().getOffset(created.getMillis())/(1000*60*60);
		
		Language lang = langIdentifier.identifyLanguageOf(tweet.getText());
		int rtCount = 0;
		int follCount = 0;
		
		results.add(new TwitterPost(0, lat, lon, content, created, created, timezone, null, tweet.getId(), tweet.getFromUserId(), tweet.getLocation(), coordinatesEstimated,lang, rtCount, follCount));
	}
	
}
