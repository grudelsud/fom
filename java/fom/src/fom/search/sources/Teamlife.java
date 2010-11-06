package fom.search.sources;

import java.util.Date;
import java.util.List;

import fom.model.SearchResult;


public class Teamlife implements GeoCapableSource {

	@Override
	public List<SearchResult> searchPosts(List<String> terms, Date startTime,
			Date endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SearchResult> geoSearchPosts(float lat, float lon, int radius,
			Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		return null;
	}

}
