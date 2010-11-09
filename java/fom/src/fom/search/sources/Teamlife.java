package fom.search.sources;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import fom.model.Post;


public class Teamlife implements Source {

	@Override
	public List<Post> searchPosts(List<String> terms, DateTime startTime, DateTime endTime) {
		return new ArrayList<Post>();
	}

	@Override
	public List<Post> geoSearchPosts(double lat, double lon, int radius, DateTime startTime, DateTime endTime) {
		return new ArrayList<Post>();
	}

}
