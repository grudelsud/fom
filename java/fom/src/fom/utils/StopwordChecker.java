package fom.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StopwordChecker {

	Set<String> stopwords;
	
	public StopwordChecker(){
		try {
			stopwords = new HashSet<String>();
			File stopwordFile = new File("data/stopwords/stopwords_en.txt");
			BufferedReader bufRead = new BufferedReader(new FileReader(stopwordFile));
			String stopword = null;
			if(bufRead.ready()){
				while((stopword=bufRead.readLine())!=null){
					stopwords.add(stopword);
				}
				bufRead.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isStopword(String word){
		for(String stopword : stopwords){
			if(word.equalsIgnoreCase(stopword))
				return true;
		}
		return false;
	}
	
}
