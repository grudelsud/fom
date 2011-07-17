package com.londondroids.fom.exec;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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
		Properties settings = new Properties();
		
		try {
			settings.load(new FileInputStream( "settings.properties" ));

			configurationBuilder.setDebugEnabled(true)
			.setOAuthConsumerKey( settings.getProperty("TwitterOAuthConsumerKey") )
			.setOAuthConsumerSecret( settings.getProperty("TwitterOAuthConsumerSecret") )
			.setOAuthAccessToken( settings.getProperty("TwitterOAuthAccessToken") )
			.setOAuthAccessTokenSecret( settings.getProperty("TwitterOAuthAccessTokenSecret") );

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
