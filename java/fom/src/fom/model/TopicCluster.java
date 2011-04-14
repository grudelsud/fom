package fom.model;

import java.util.HashMap;
import java.util.Map;

public class TopicCluster extends Cluster {
			
	private double score;
	
	public TopicCluster(Query originatingQuery, Cluster parent, double score) {
		super(originatingQuery, parent);
		this.score = score;
	}

	@Override
	public Map<String, String> getMeta() {
		Map<String, String> meta = new HashMap<String, String>();
		meta.put("type", "topic");
		meta.put("score", Double.toString(score));
		return meta;
	}

	@Override
	public int getTypeId() {
		return 3;
	}

}
