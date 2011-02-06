package fom.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

public class TimeCluster extends Cluster {

	private DateTime startTime;
	private DateTime endTime;
	
	public TimeCluster(Query originatingQuery) {
		super(originatingQuery, null);
		startTime = null;
		endTime = null;
	}

	@Override
	public Map<String, String> getMeta() {
		Map<String, String> meta = new HashMap<String, String>();
		meta.put("type", "time");
		meta.put("startTime", getStartTime().toString());			
		meta.put("endTime", getEndTime().toString());			
		return meta;
	}

	public DateTime getStartTime() {
		if(startTime==null){
			Collections.sort(this.getPosts(), new Comparator<Post>() {
				
				@Override
				public int compare(Post o1, Post o2) {
					return o1.getCreated().compareTo(o2.getCreated());
				}
			});
			//TODO: throw exception if cluster is empty
			this.startTime = this.getPosts().get(0).getCreated();
		}
		return startTime;
	}

	public DateTime getEndTime() {
		if(endTime==null){
			Collections.sort(this.getPosts(), new Comparator<Post>() {
				
				@Override
				public int compare(Post o1, Post o2) {
					return o1.getCreated().compareTo(o2.getCreated());
				}
			});
			//TODO: throw exception if cluster is empty
			this.endTime = this.getPosts().get(this.getPosts().size()-1).getCreated();			
		}
		return endTime;
	}
	
	public void setStartTime(DateTime startTime){
		this.startTime = startTime;
	}
	
	public void setEndTime(DateTime endTime){
		this.endTime = endTime;
	}

}
