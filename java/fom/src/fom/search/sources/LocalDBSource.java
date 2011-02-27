package fom.search.sources;

import java.util.List;

import org.joda.time.DateTime;

import fom.model.Post;
import fom.model.dao.interfaces.DAOFactory;

public class LocalDBSource implements Source {

	private String sourceName;
	private boolean considerApproxGeolocations;
	
	public LocalDBSource(){
		this.sourceName = "twitter";
		this.considerApproxGeolocations = true;
	}
	
	public void setSourceName(String sourceName){
		this.sourceName = sourceName;
	}
	
	public void setConsiderApproxGeolocations(boolean considerApproxGeolocations){
		this.considerApproxGeolocations = considerApproxGeolocations;
	}
	
	@Override
	public List<Post> searchPosts(List<String> terms, DateTime startTime, DateTime endTime, double lat, double lon, int radius) {
		return DAOFactory.getFactory().getPostDAO().retrieve(terms, startTime, endTime, lat, lon, radius, sourceName, considerApproxGeolocations);
	}

}
