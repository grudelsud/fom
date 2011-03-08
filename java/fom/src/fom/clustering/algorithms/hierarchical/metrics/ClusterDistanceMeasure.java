package fom.clustering.algorithms.hierarchical.metrics;

import fom.clustering.algorithms.DistanceMeasure;
import fom.clustering.algorithms.hierarchical.HierarchicalPostCluster;

public interface ClusterDistanceMeasure extends DistanceMeasure<HierarchicalPostCluster> {
	public double getDistance(HierarchicalPostCluster cluster1, HierarchicalPostCluster cluster2);
}
