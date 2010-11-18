package fom.model;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

public class TwitterPost extends Post {
	
	private long tweetId;
	private long twitterUserId;

	public TwitterPost(long id, double lat, double lon, String content, DateTime created, DateTime modified, int timezone, Place location, long tweetId, int twitterUserId) {
		super(id, lat, lon, content, created, modified, timezone, location);
		this.tweetId = tweetId;
		this.twitterUserId = twitterUserId;
	}

	@Override
	public Map<String, String> getMeta() {
		Map<String, String> meta = new HashMap<String, String>();
		meta.put("tweetId", new Long(tweetId).toString());
		meta.put("twitterUserId", new Long(twitterUserId).toString());
		return meta;
	}

	@Override
	public String getSourceName() {
		return "twitter";
	}

	@Override
	public long getSourceId() {
		return tweetId;
	}

}
