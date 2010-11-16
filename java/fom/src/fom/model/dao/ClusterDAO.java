package fom.model.dao;

import fom.model.Cluster;

public interface ClusterDAO {
	public long create(Cluster theCluster);
	public Cluster retrieve(long clusterId);
}
