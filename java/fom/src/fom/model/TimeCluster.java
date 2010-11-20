package fom.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;

public class TimeCluster extends Cluster {

	
	public TimeCluster(Query originatingQuery) {
		super(originatingQuery);
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
		Collections.sort(this.getPosts(), new Comparator<Post>() {

			@Override
			public int compare(Post o1, Post o2) {
				return o1.getCreated().compareTo(o2.getCreated());
			}
		});
		//TODO: throw exception if cluster is empty
		return this.getPosts().get(0).getCreated();
	}

	public DateTime getEndTime() {
		Collections.sort(this.getPosts(), new Comparator<Post>() {

			@Override
			public int compare(Post o1, Post o2) {
				return o1.getCreated().compareTo(o2.getCreated());
			}
		});
		//TODO: throw exception if cluster is empty
		return this.getPosts().get(this.getPosts().size()-1).getCreated();
	}

}
