package fom.streamcapturing;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.joda.time.DateTime;

import fom.application.StreamCapturer;
import fom.model.Place;
import fom.model.Post;
import fom.model.TwitterPost;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

public class TwitterStatusListener implements StatusListener {
	private StreamCapturer streamCapturer;
	private int tweetCount;
	private boolean filterGeoTagged;
	private BlockingQueue<Post> postQueue;
	
	public TwitterStatusListener(StreamCapturer streamCapturer, boolean filterGeoTagged){
		this.streamCapturer = streamCapturer;
		this.filterGeoTagged = filterGeoTagged;
		this.postQueue = new ArrayBlockingQueue<Post>(1000);
		new Thread(new PostQueueProcessor(postQueue)).start();
	}
	
    public void onStatus(Status status) {
    	if(filterGeoTagged){
    		if(status.getGeoLocation()==null){
    			return;
    		}
    	}
    	savePost(status);
    	if(++tweetCount%100==0){
    		System.out.print(".");
    	}
    	if(tweetCount==1000){
    		System.out.println();
    		tweetCount=0;
    	}
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
		try {
			postQueue.put(post);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
    //	System.err.println("Deletion notice for " + statusDeletionNotice.getStatusId());
    }
    
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
    	System.err.println("Track limitation: " + numberOfLimitedStatuses);
    }
    
    public void onScrubGeo(int arg0, long arg1) {
    //	System.err.println("Should scrub geo info from posts of " + arg0 + " until postId: " + arg1);
    }
    
    public void onException(Exception ex){
    	streamCapturer.handleException(ex);
    }
}
