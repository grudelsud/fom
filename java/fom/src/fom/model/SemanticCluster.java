package fom.model;

import java.util.HashMap;
import java.util.Map;

import fom.model.dao.interfaces.DAOFactory;

public class SemanticCluster extends Cluster {
			
	public SemanticCluster(Query originatingQuery, Cluster parent) {
		super(originatingQuery, parent);		
	}

	@Override
	public Map<String, String> getMeta() {
		Map<String, String> meta = new HashMap<String, String>();
		meta.put("type", "semantic");
		long parentClusterID = this.getParentCluster().getId();
		if(parentClusterID==0){
			parentClusterID = DAOFactory.getFactory().getClusterDAO().create(getParentCluster());
		}
		meta.put("parent", Long.toString(parentClusterID));
		return meta;	
	}

}
