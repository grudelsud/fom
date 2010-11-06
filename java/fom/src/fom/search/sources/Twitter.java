package fom.search.sources;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.domain.FeedTitle;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Tweet;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import controller.helper.StringOperations;

import fom.model.SearchResult;


public class Twitter implements GeoCapableSource {

	@Override
	public List<SearchResult> searchPosts(List<String> terms, Date startTime,
			Date endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SearchResult> geoSearchPosts(float lat, float lon, int radius,
			Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	
	//TODO!!!
	private void search(String keyword, Calendar since, Long maxId) throws InterruptedException{
		Twitter twitter = new TwitterFactory().getInstance();
		Query query = new Query();
		query.setRpp(100);
		query.setQuery(StringOperations.hashtagify(keyword).concat(" twitpic OR tweetphoto"));
		query.setSince(new SimpleDateFormat("yyyy-MM-dd").format(since.getTime()));
		if(maxId!=null) query.setMaxId(maxId);
		if(daysToSearch<7) query.setLang("en");
		QueryResult result;
		for(int i=1; i<16; i++){
			query.setPage(i);
			try{
				result = twitter.search(query);
				System.out.println("Found " + result.getTweets().size() + " tweets");
				for(Tweet tweet : result.getTweets()){
					resultsQueue.put(new SearchResult(tweet, keyword, feedTitle));
				}
				if(result.getTweets().size()<100){
					break;
				}
				if(i==15 && result.getTweets().size()==100){
					search(keyword, since, feedTitle, result.getTweets().get(result.getTweets().size()-1).getId());
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
	
}
