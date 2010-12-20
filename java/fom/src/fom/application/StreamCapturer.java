package fom.application;

import org.joda.time.DateTime;

import fom.model.Place;
import fom.model.Post;
import fom.model.TwitterPost;
import fom.model.dao.interfaces.PostDAO;
import fom.model.dao.localdb.LocalDBDAOFactory;
import fom.properties.PropertyHandler;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class StreamCapturer implements Runnable {
	
	StatusListener listener;
	TwitterStream twitterStream;
	
	public void run() {
		setupCapturer();
	    twitterStream.sample();
	}
	
	private void setupCapturer(){
		listener = new TwitterStatusListener(this);
	    ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setDebugEnabled(true)
	      .setOAuthConsumerKey(PropertyHandler.getInstance().getProperties().getProperty("TwitterOAuthConsumerKey"))
	      .setOAuthConsumerSecret(PropertyHandler.getInstance().getProperties().getProperty("TwitterOAuthConsumerSecret"))
	      .setOAuthAccessToken(PropertyHandler.getInstance().getProperties().getProperty("TwitterOAuthAccessToken"))
	      .setOAuthAccessTokenSecret(PropertyHandler.getInstance().getProperties().getProperty("TwitterOAuthAccessTokenSecret"));
	    twitterStream = new  TwitterStreamFactory(cb.build()).getInstance();
	    twitterStream.addListener(listener);
	}
	
	private void handleException(Exception ex) {
		ex.printStackTrace();
		System.out.println("There has been some kind of error, waiting 1 minute and restarting...");
		twitterStream.shutdown();
		try {
			Thread.sleep(1*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    twitterStream.sample();
	}
	
	private class TwitterStatusListener implements StatusListener{
    	private PostDAO postDAO = LocalDBDAOFactory.getFactory().getPostDAO(); //TODO: Use General DAO Factory!
    	private StreamCapturer streamCapturer;
    	private int tweetCount;
    	
    	public TwitterStatusListener(StreamCapturer streamCapturer){
    		this.streamCapturer = streamCapturer;
    	}
    	
        public void onStatus(Status status) {
        	savePost(status);
        	if(++tweetCount%100==0){
        		System.out.print(".");
        	}
        	if(tweetCount==1000){
        		System.out.println();
        		tweetCount=0;
        	}
        }
        public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
     //   	System.err.println("Deletion notice for " + statusDeletionNotice.getStatusId());
        }
        public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
        	System.out.println();
        	System.err.println("Track limitation: " + numberOfLimitedStatuses);
        }
		public void onScrubGeo(int arg0, long arg1) {
	//		System.err.println("Should scrub geo info from posts of " + arg0 + " until postId: " + arg1);
		}
        public void onException(Exception ex){
           streamCapturer.handleException(ex);
        }
        private void savePost(Status status){
        	double lat=0;
        	double lon=0;
        	if(status.getGeoLocation()!=null){
        		lat = status.getGeoLocation().getLatitude();
        		lon = status.getGeoLocation().getLongitude();
        	}
        	DateTime created = new DateTime(status.getCreatedAt());
    		int timezone = created.getZone().getOffset(created.getMillis())/(1000*60*60);
            Place place = null;
            if(status.getPlace()!=null){
            	place = new Place(0, 0, status.getPlace().getURL(), status.getPlace().getPlaceType());
            }
    		Post post = new TwitterPost(0, lat, lon, status.getText(), created, created, timezone, place, status.getId(), status.getUser().getId());
            postDAO.create(post);
        }
	}
}