package fom.clustering.algorithms;

import java.util.List;

public interface Clusterer<ObjectType> {
	
	public List<List<ObjectType>> performClustering(List<ObjectType> data);
	
}
