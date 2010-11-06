package fom.clustering.algorithms.kmedoids.metrics;

import java.util.ArrayList;
import java.util.List;

import com.wcohen.ss.BasicStringWrapperIterator;
import com.wcohen.ss.TFIDF;
import com.wcohen.ss.api.StringWrapper;
import com.wcohen.ss.tokens.SimpleTokenizer;

import fom.utils.StringOperations;


public class TFIDFSimilarity extends AbstractSimilarity<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4532115480695009048L;
	private TFIDF tfidf;
	private double[] similarities;
	
	@Override
	public double getMeasure(int firstObjectIndex, int secondObjectIndex, String[] objects) {
		if(firstObjectIndex>secondObjectIndex){
			int swapIndex = firstObjectIndex;
			firstObjectIndex = secondObjectIndex;
			secondObjectIndex = swapIndex;
		}
		double similarity = similarities[(firstObjectIndex+1)*(2*objects.length-firstObjectIndex)/2-objects.length+secondObjectIndex];
		if(similarity==-1){
	        similarity = tfidf.score(tfidf.prepare(objects[firstObjectIndex]), tfidf.prepare(objects[secondObjectIndex]));
			similarities[(firstObjectIndex+1)*(2*objects.length-firstObjectIndex)/2-objects.length+secondObjectIndex] = similarity;
		}
		return similarity;    
	}

	@Override
	public void initialize(String[] objects) {
		int arrayDimension = objects.length*(objects.length+1)/2;
		similarities = new double[arrayDimension];
		for(int i=0; i<arrayDimension; i++){
			similarities[i]=-1;
		}
		List<StringWrapper> documents = new ArrayList<StringWrapper>();
		tfidf = new TFIDF(new SimpleTokenizer(true, true));
		for(String document : objects){
			documents.add(tfidf.prepare(document));
		}
		tfidf.train(new BasicStringWrapperIterator(documents.iterator()));
	}

}
