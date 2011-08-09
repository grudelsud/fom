package fom.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fom.langidentification.LanguageIdentifier.Language;

public class StopwordChecker {

	private Set<String> stopwords;
	
	private static Map<Language, StopwordChecker> instances = new HashMap<Language, StopwordChecker>();
	
	private StopwordChecker(Language lang){
		try {
			stopwords = new HashSet<String>();
			File stopwordFile = new File("data/stopwords/" + lang.toString() + ".txt");
			BufferedReader bufRead = new BufferedReader(new FileReader(stopwordFile));
			String stopword = null;
			if(bufRead.ready()){
				while((stopword=bufRead.readLine())!=null){
					stopwords.add(stopword.toLowerCase().trim());
				}
				bufRead.close();
			}
		} catch (FileNotFoundException e) {
			System.err.println("Stopword list not available for language: " + lang.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static StopwordChecker getCheckerForLanguage(Language lang){
		StopwordChecker checker = instances.get(lang);
		if(checker==null){
			checker = new StopwordChecker(lang);
			instances.put(lang, checker);
		}
		return checker;
	}
	
	public boolean isStopword(String word){
		for(String stopword : stopwords){
			if(word.equalsIgnoreCase(stopword))
				return true;
		}
		return false;
	}
	
}
