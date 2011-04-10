package fom.model;

import java.util.HashMap;
import java.util.Map;

public class RelatedLinksCluster extends Cluster {

	public RelatedLinksCluster(Query originatingQuery, Cluster parent) {
		super(originatingQuery, parent);
	}

	@Override
	public Map<String, String> getMeta() {
		Map<String, String> meta = new HashMap<String, String>();
		meta.put("type", "relatedLinks");
		return meta;
	}

	@Override
	public int getTypeId() {
		return 4;
	}

}
