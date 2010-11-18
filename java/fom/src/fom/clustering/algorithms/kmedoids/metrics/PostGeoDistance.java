package fom.clustering.algorithms.kmedoids.metrics;

import fom.model.Post;

public class PostGeoDistance extends AbstractDistance<Post> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2126867601418707453L;
	double[] distances;
	
	
	@Override
	public void initialize(Post[] objects) {
		int arrayDimension = objects.length*(objects.length+1)/2;
		distances = new double[arrayDimension];
		for(int i=0; i<arrayDimension; i++){
			distances[i]=-1;
		}		
	}

	@Override
	public double getMeasure(int firstObjectIndex, int secondObjectIndex, Post[] objects) {
		if(firstObjectIndex>secondObjectIndex){
			int swapIndex = firstObjectIndex;
			firstObjectIndex = secondObjectIndex;
			secondObjectIndex = swapIndex;
		}		
		double distance = distances[(firstObjectIndex+1)*(2*objects.length-firstObjectIndex)/2-objects.length+secondObjectIndex];
		if(distance==-1){
			// ACOS(SIN(lat1)*SIN(lat2)+COS(lat1)*COS(lat2)*COS(lon2-lon1))*6371
			double lat1 = Math.toRadians(objects[firstObjectIndex].getLat());
			double lon1 = Math.toRadians(objects[firstObjectIndex].getLon());
			double lat2 = Math.toRadians(objects[secondObjectIndex].getLat());
			double lon2 = Math.toRadians(objects[secondObjectIndex].getLon());
			
			distance =  Math.acos(Math.sin(lat1)*Math.sin(lat2)+Math.cos(lat1)*Math.cos(lat2)*Math.cos(lon2-lon1))*6371;
			distances[(firstObjectIndex+1)*(2*objects.length-firstObjectIndex)/2-objects.length+secondObjectIndex] = distance;
		}
		return distance;
	}


}
