package fom.application;

import fom.properties.PropertyHandler;
import fom.streamcapturing.TwitterStatusListener;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class StreamCapturer implements Runnable {
	
	private boolean filterGeoTagged;
	private StatusListener listener;
	private TwitterStream twitterStream;
	
	public StreamCapturer(boolean filterGeoTagged){
		this.filterGeoTagged = filterGeoTagged;
	}
	
	public void run() {
		setupCapturer();
	    twitterStream.sample();
	}
	
	private void setupCapturer(){
		listener = new TwitterStatusListener(this, filterGeoTagged);
	    ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setDebugEnabled(true)
	      .setOAuthConsumerKey(PropertyHandler.getStringProperty("TwitterOAuthConsumerKey"))
	      .setOAuthConsumerSecret(PropertyHandler.getStringProperty("TwitterOAuthConsumerSecret"))
	      .setOAuthAccessToken(PropertyHandler.getStringProperty("TwitterOAuthAccessToken"))
	      .setOAuthAccessTokenSecret(PropertyHandler.getStringProperty("TwitterOAuthAccessTokenSecret"));
	    twitterStream = new  TwitterStreamFactory(cb.build()).getInstance();
	    twitterStream.addListener(listener);
	}
	
	public void handleException(Exception ex) {
		ex.printStackTrace();
		System.out.println("There has been some kind of error, waiting 1 minute and restarting...");
		twitterStream.shutdown();
		try {
			Thread.sleep(60*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	    twitterStream.sample();
	}
}