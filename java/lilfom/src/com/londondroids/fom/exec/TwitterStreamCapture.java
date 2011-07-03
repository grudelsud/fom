package com.londondroids.fom.exec;

import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamCapture {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		
		configurationBuilder.setDebugEnabled(true)
			.setOAuthConsumerKey("*********************")
			.setOAuthConsumerSecret("******************************************")
			.setOAuthAccessToken("**************************************************")
			.setOAuthAccessTokenSecret("******************************************");
		
		TwitterStream twitterStream = new TwitterStreamFactory( configurationBuilder.build() ).getInstance();
		StatusListener statusListener = new StatusListener() {
			
			@Override
			public void onStatus(Status status)
			{
				System.out.println( status.getText() );
			}
			
			@Override
			public void onException(Exception ex)
			{
				System.err.println( ex.getMessage() );
			}
			
			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub
				
			}
		};
		twitterStream.addListener( statusListener );
		twitterStream.sample();
	}

}
