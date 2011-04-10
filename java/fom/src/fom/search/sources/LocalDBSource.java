package fom.search.sources;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import fom.model.Post;
import fom.model.TwitterPost;
import fom.model.dao.interfaces.DAOFactory;

public class LocalDBSource implements Source {

	private String sourceName;
	private boolean considerApproxGeolocations;
	private int minRTCount;
	private int minFollCount;
	
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
	
	public void setMinRTCount(int minRTCount){
		this.minRTCount = minRTCount;
	}
	
	public void setMinFollCount(int minFollCount) {
		this.minFollCount = minFollCount;
	}
	
	@Override
	public List<Post> searchPosts(List<String> terms, DateTime startTime, DateTime endTime, double lat, double lon, int radius) {
		List<Post> posts =  DAOFactory.getFactory().getPostDAO().retrieve(terms, startTime, endTime, lat, lon, radius, sourceName, considerApproxGeolocations);
		if(this.sourceName.equalsIgnoreCase("twitter")){
			List<Post> res = new ArrayList<Post>();
			for(int i=0; i<posts.size(); i++){
				TwitterPost twPost = (TwitterPost) posts.get(i);
				if(twPost.getRtCount()>=minRTCount && twPost.getFollowerCount()>=minFollCount){
					res.add(twPost);
				}
			}
			return res;
		} else {
			return posts;
		}
	}

}
