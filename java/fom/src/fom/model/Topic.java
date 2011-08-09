package fom.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import fom.langidentification.LanguageIdentifier.Language;

public class Topic {
	private List<String> words;
	private Map<String, Float> scores;
	private double alpha;
	private Language language;
	
	public Topic(double alpha, Language language){
		this.words = new ArrayList<String>();
		this.scores = new Hashtable<String, Float>();
		this.alpha = alpha;
		this.language = language;
	}
	
	public double getAlpha(){
		return alpha;
	}
	
	public void addWord(String word){
		this.words.add(word);
	}

	public void addWord(String word, Float score){
		this.words.add(word);
		this.scores.put(word, score);
	}

	public List<String> getWords(){
		return words;
	}
	
	public Map<String, Float> getScores() {
		return scores;
	}
	
	public Language getLanguage(){
		return language;
	}
}
