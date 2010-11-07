package fom.search.sources;

import java.util.List;

import org.joda.time.DateTime;

import fom.model.Post;


public interface GeoCapableSource extends Source{

	public List<Post> geoSearchPosts(float lat, float lon, int radius, DateTime startTime, DateTime endTime);
	
}
