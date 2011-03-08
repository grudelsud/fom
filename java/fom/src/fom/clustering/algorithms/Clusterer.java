package fom.clustering.algorithms;

import java.util.List;

import fom.model.Post;

public interface Clusterer {
	
	public List<List<Post>> performClustering(List<Post> data);
	
}
