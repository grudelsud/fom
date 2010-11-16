package fom.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

public class TimeCluster extends Cluster {

	private DateTime startTime;
	private DateTime endTime;
	
	@Override
	public Map<String, String> getMeta() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Term> getClusterTerms() {
		return new ArrayList<Term>();
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}

}
