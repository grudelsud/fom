package fom.clustering.algorithms.kmedoids.metrics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class TagVectorCosineSimilarity extends AbstractSimilarity< ArrayList<String> > {

	public TagVectorCosineSimilarity(List<ArrayList<String>> objects) {
		super(objects);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4532115480695009048L;
	double[] similarities;
	
	@Override
	public double getMeasure(int firstObjectIndex, int secondObjectIndex) {
		if(firstObjectIndex>secondObjectIndex){
			int swapIndex = firstObjectIndex;
			firstObjectIndex = secondObjectIndex;
			secondObjectIndex = swapIndex;
		}
		double similarity = similarities[(firstObjectIndex+1)*(2*objects.size()-firstObjectIndex)/2-objects.size()+secondObjectIndex];
		if(similarity==-1){
			final ArrayList<String> str1Tokens = objects.get(firstObjectIndex);
			final ArrayList<String> str2Tokens = objects.get(secondObjectIndex);

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

			similarities[(firstObjectIndex+1)*(2*objects.size()-firstObjectIndex)/2-objects.size()+secondObjectIndex] = similarity;
		}
		return similarity;    
	}

	@Override
	public void initialize() {
		int arrayDimension = objects.size()*(objects.size()+1)/2;
		similarities = new double[arrayDimension];
		for(int i=0; i<arrayDimension; i++){
			similarities[i]=-1;
		}
	}

}
