package fom.model;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

public class GenericPost extends Post {

	public GenericPost(long id, double lat, double lon, String content, DateTime created, DateTime modified, int timezone, Place place, String userLocation, boolean coordinatesEstimated) {
		super(id, lat, lon, content, created, modified, timezone, place, userLocation, coordinatesEstimated);
	}

	@Override
	public Map<String, String> getMeta() {
		return new HashMap<String, String>();
	}

	@Override
	public String getSourceName() {
		return "Post";
	}

	@Override
	public long getSourceId() {
		return 0;
	}

}
