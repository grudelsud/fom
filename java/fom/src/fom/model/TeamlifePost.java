package fom.model;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

import fom.langidentification.LanguageIdentifier.Language;

public class TeamlifePost extends Post {


	public TeamlifePost(long id, double lat, double lon, String content, DateTime created, DateTime modified, int timezone, Place location, String userLocation, boolean coordinatesEstimated, Language language) {
		super(id, lat, lon, content, created, modified, timezone, location, userLocation, coordinatesEstimated, language);
	}

	@Override
	public Map<String, String> getMeta() {
		return new HashMap<String, String>();
	}

	@Override
	public String getSourceName() {
		return "teamlife";
	}

	@Override
	public long getSourceId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
