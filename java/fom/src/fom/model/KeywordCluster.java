package fom.model;

import java.util.HashMap;
import java.util.Map;

public class KeywordCluster extends Cluster {

	public KeywordCluster(Query originatingQuery, Cluster parent) {
		super(originatingQuery, parent);
	}

	@Override
	public Map<String, String> getMeta() {
		Map<String, String> meta = new HashMap<String, String>();
		meta.put("type", "keywords");
		return meta;
	}

	@Override
	public int getTypeId() {
		return 4;
	}

}
