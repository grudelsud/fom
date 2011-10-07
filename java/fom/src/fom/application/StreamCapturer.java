package fom.application;

import fom.properties.PropertyHandler;
import fom.streamcapturing.TwitterStatusListener;
import twitter4j.FilterQuery;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class StreamCapturer implements Runnable {
	
	private boolean filterGeoTagged;
	private StatusListener listener;
	private TwitterStream twitterStream;
	private String[] trackKeywords;
	private double[][] geoBoxes;
	private boolean sample;
	
	public StreamCapturer(boolean filterGeoTagged, String[] trackKeywords, double[][] geoBoxes){
		this.filterGeoTagged = filterGeoTagged;
		this.trackKeywords = trackKeywords;
		this.geoBoxes = geoBoxes;
		if(trackKeywords.length==0 && geoBoxes.length==0){
			this.sample = true;
		} else {
			this.sample = false;
		}
	}
	
	public void run() {
		setupCapturer();
		if(sample){
			twitterStream.sample();
		} else {
			twitterStream.filter(new FilterQuery(0, new long[0], trackKeywords, geoBoxes));			
		}
	}
	
	private void setupCapturer(){
		listener = new TwitterStatusListener(this, filterGeoTagged);
	    ConfigurationBuilder cb = new ConfigurationBuilder();
	    cb.setDebugEnabled(false)
	      .setUseSSL(true)
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
		if(sample){
			twitterStream.sample();
		} else {
			twitterStream.filter(new FilterQuery(0, new long[0], trackKeywords, geoBoxes));			
		}
	}
}