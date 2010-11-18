package fom.model;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

public class TimeCluster extends Cluster {

	private DateTime startTime;
	private DateTime endTime;
	
	public TimeCluster(Query originatingQuery) {
		super(originatingQuery);
	}
	
	public TimeCluster(Query originatingQuery, DateTime startTime, DateTime endTime) {
		super(originatingQuery);
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	public Map<String, String> getMeta() {
		Map<String, String> meta = new HashMap<String, String>();
		meta.put("type", "time");
		if(startTime != null){
			meta.put("startTime", startTime.toString());			
		}
		if(endTime != null){
			meta.put("endTime", endTime.toString());			
		}
		return meta;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

}
