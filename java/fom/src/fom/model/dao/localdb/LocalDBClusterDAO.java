package fom.model.dao.localdb;

import java.sql.Connection;

import fom.model.Cluster;
import fom.model.dao.ClusterDAO;

public class LocalDBClusterDAO implements ClusterDAO {
	
	Connection conn;

	public LocalDBClusterDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public long create(Cluster theCluster) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Cluster retrieve(long clusterId) {
		// TODO Auto-generated method stub
		return null;
	}	


}
