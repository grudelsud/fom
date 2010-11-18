package fom.model;

import java.util.HashMap;
import java.util.Map;

public class SemanticCluster extends Cluster {
		
	public SemanticCluster(Query originatingQuery) {
		super(originatingQuery);
	}

	@Override
	public Map<String, String> getMeta() {
		Map<String, String> meta = new HashMap<String, String>();
		meta.put("type", "semantic");
		return meta;	
	}

}
