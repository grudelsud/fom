package fom.model;

import java.util.ArrayList;
import java.util.List;

public class Topic {
	private List<String> words;
	private double alpha;
	
	public Topic(double alpha){
		this.words = new ArrayList<String>();
		this.alpha = alpha;
	}
	
	public double getAlpha(){
		return alpha;
	}
	
	public void addWord(String word){
		this.words.add(word);
	}
	
	public List<String> getWords(){
		return words;
	}
}
