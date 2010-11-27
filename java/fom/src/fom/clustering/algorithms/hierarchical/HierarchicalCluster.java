package fom.clustering.algorithms.hierarchical;

import java.util.ArrayList;
import java.util.List;

public class HierarchicalCluster<ObjectType> {
	private List<ObjectType> clusterObjects;
	
	public HierarchicalCluster(ObjectType firstObject){
		this.clusterObjects = new ArrayList<ObjectType>();
		clusterObjects.add(firstObject);
	}
	
	public void mergeWith(HierarchicalCluster<ObjectType> cluster){
		clusterObjects.addAll(cluster.getObjects());
	}
	
	public List<ObjectType> getObjects(){
		return clusterObjects;
	}
}
