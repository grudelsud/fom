package fom.model;

import java.util.ArrayList;
import java.util.List;

import fom.langidentification.LanguageIdentifier.Language;

public class Topic {
	private List<String> words;
	private double alpha;
	private Language language;
	
	public Topic(double alpha, Language language){
		this.words = new ArrayList<String>();
		this.alpha = alpha;
		this.language = language;
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
	
	public Language getLanguage(){
		return language;
	}
}
