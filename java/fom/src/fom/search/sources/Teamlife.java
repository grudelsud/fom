package fom.search.sources;

import java.util.List;

import org.joda.time.DateTime;

import fom.model.Post;


public class Teamlife implements GeoCapableSource {

	@Override
	public List<Post> searchPosts(List<String> terms, DateTime startTime, DateTime endTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Post> geoSearchPosts(float lat, float lon, int radius, DateTime startTime, DateTime endTime) {
		// TODO Auto-generated method stub
		return null;
	}

}
