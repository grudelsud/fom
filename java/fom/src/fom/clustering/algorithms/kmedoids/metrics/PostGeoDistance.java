package fom.clustering.algorithms.kmedoids.metrics;

import java.util.List;

import fom.model.Post;

public class PostGeoDistance extends AbstractDistance<Post> {

	public PostGeoDistance(List<Post> objects) {
		super(objects);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2126867601418707453L;
	double[] distances;
	
	
	@Override
	public void initialize() {
		int arrayDimension = objects.size()*(objects.size()+1)/2;
		distances = new double[arrayDimension];
		for(int i=0; i<arrayDimension; i++){
			distances[i]=-1;
		}		
	}

	@Override
	public double getMeasure(int firstObjectIndex, int secondObjectIndex) {
		if(firstObjectIndex>secondObjectIndex){
			int swapIndex = firstObjectIndex;
			firstObjectIndex = secondObjectIndex;
			secondObjectIndex = swapIndex;
		}		
		double distance = distances[(firstObjectIndex+1)*(2*objects.size()-firstObjectIndex)/2-objects.size()+secondObjectIndex];
		if(distance==-1){
			// ACOS(SIN(lat1)*SIN(lat2)+COS(lat1)*COS(lat2)*COS(lon2-lon1))*6371
			double lat1 = Math.toRadians(objects.get(firstObjectIndex).getLat());
			double lon1 = Math.toRadians(objects.get(firstObjectIndex).getLon());
			double lat2 = Math.toRadians(objects.get(secondObjectIndex).getLat());
			double lon2 = Math.toRadians(objects.get(secondObjectIndex).getLon());
			
			distance =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*6371;
			distances[(firstObjectIndex+1)*(2*objects.size()-firstObjectIndex)/2-objects.size()+secondObjectIndex] = distance;
		}
		return distance;
	}


}
