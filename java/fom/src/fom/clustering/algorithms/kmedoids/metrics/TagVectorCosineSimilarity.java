package fom.clustering.algorithms.kmedoids.metrics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



public class TagVectorCosineSimilarity extends AbstractSimilarity< ArrayList<String> > {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4532115480695009048L;
	double[] similarities;
	
	@Override
	public double getMeasure(int firstObjectIndex, int secondObjectIndex, ArrayList<String>[] objects) {
		if(firstObjectIndex>secondObjectIndex){
			int swapIndex = firstObjectIndex;
			firstObjectIndex = secondObjectIndex;
			secondObjectIndex = swapIndex;
		}
		double similarity = similarities[(firstObjectIndex+1)*(2*objects.length-firstObjectIndex)/2-objects.length+secondObjectIndex];
		if(similarity==-1){
			final ArrayList<String> str1Tokens = objects[firstObjectIndex];
			final ArrayList<String> str2Tokens = objects[secondObjectIndex];

	        final Set<String> allTokens = new HashSet<String>();
	        allTokens.addAll(str1Tokens);
	        final int termsInString1 = allTokens.size();
	        final Set<String> secondStringTokens = new HashSet<String>();
	        secondStringTokens.addAll(str2Tokens);
	        final int termsInString2 = secondStringTokens.size();

	        //now combine the sets
	        allTokens.addAll(secondStringTokens);
	        final int commonTerms = (termsInString1 + termsInString2) - allTokens.size();

	        //compute CosineSimilarity
	        similarity = (float) (commonTerms) / (float) (Math.pow((float) termsInString1, 0.5f) * Math.pow((float) termsInString2, 0.5f));

			similarities[(firstObjectIndex+1)*(2*objects.length-firstObjectIndex)/2-objects.length+secondObjectIndex] = similarity;
		}
		return similarity;    
	}

	@Override
	public void initialize(ArrayList<String>[] objects) {
		int arrayDimension = objects.length*(objects.length+1)/2;
		similarities = new double[arrayDimension];
		for(int i=0; i<arrayDimension; i++){
			similarities[i]=-1;
		}
	}

}
