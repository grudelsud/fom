package fom.clustering.algorithms.hierarchical.metrics;

import fom.clustering.algorithms.DistanceMeasure;
import fom.clustering.algorithms.hierarchical.HierarchicalCluster;

public interface ClusterDistanceMeasure<ObjectType> extends DistanceMeasure<HierarchicalCluster<ObjectType>> {
	public double getDistance(HierarchicalCluster<ObjectType> cluster1, HierarchicalCluster<ObjectType> cluster2);
}
