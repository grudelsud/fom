package fom.clustering.algorithms.kmedoids.metrics;

import java.util.ArrayList;
import java.util.List;

import com.wcohen.ss.BasicStringWrapperIterator;
import com.wcohen.ss.TFIDF;
import com.wcohen.ss.api.StringWrapper;
import com.wcohen.ss.tokens.SimpleTokenizer;

import fom.model.Post;

public class TFIDFSimilarity extends AbstractSimilarity<Post> {

	public TFIDFSimilarity(List<Post> objects) {
		super(objects);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4532115480695009048L;
	private TFIDF tfidf;
	private double[] similarities;
	
	@Override
	public double getMeasure(int firstObjectIndex, int secondObjectIndex) {
		if(firstObjectIndex>secondObjectIndex){
			int swapIndex = firstObjectIndex;
			firstObjectIndex = secondObjectIndex;
			secondObjectIndex = swapIndex;
		}
		double similarity = similarities[(firstObjectIndex+1)*(2*objects.size()-firstObjectIndex)/2-objects.size()+secondObjectIndex];
		if(similarity==-1){
	        similarity = tfidf.score(tfidf.prepare(objects.get(firstObjectIndex).getContent()), tfidf.prepare(objects.get(secondObjectIndex).getContent()));
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
		List<StringWrapper> documents = new ArrayList<StringWrapper>();
		tfidf = new TFIDF(new SimpleTokenizer(true, true));
		for(Post post : objects){
			documents.add(tfidf.prepare(post.getContent()));
		}
		tfidf.train(new BasicStringWrapperIterator(documents.iterator()));
	}

}
