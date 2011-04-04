package fom.model;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import fom.langidentification.LanguageIdentifier.Language;

public class TwitterPost extends Post {
	
	private long tweetId;
	private long twitterUserId;
	private long rtCount;
	private int followerCount;

	public TwitterPost(long id, double lat, double lon, String content, DateTime created, DateTime modified, int timezone, Place location, long tweetId, long twitterUserId, String userLocation, boolean coordinatesEstimated, Language language, long rtCount, int followerCount) {
		super(id, lat, lon, content, created, modified, timezone, location, userLocation, coordinatesEstimated, language);
		this.tweetId = tweetId;
		this.twitterUserId = twitterUserId;
		this.rtCount = rtCount;
		this.followerCount = followerCount;
	}

	@Override
	public Map<String, String> getMeta() {
		Map<String, String> meta = new HashMap<String, String>();
		meta.put("tweetId", new Long(tweetId).toString());
		meta.put("twitterUserId", new Long(twitterUserId).toString());
		meta.put("rtCount", new Long(rtCount).toString());
		meta.put("followerCount", new Integer(followerCount).toString());
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

	public long getTweetId() {
		return tweetId;
	}

	public long getTwitterUserId() {
		return twitterUserId;
	}

	public long getRtCount() {
		return rtCount;
	}

	public int getFollowerCount() {
		return followerCount;
	}

}
