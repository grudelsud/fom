package fom.search.sources;

import java.util.Date;
import java.util.List;

import fom.model.SearchResult;

public interface GeoCapableSource extends Source{

	public List<SearchResult> geoSearchPosts(float lat, float lon, int radius, Date startTime, Date endTime);
	
}
