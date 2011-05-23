package fom.rss;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fom.langidentification.LanguageIdentifier.Language;
import fom.model.Feed;
import fom.model.Term;
import fom.model.Vocabulary;

public class FeedListParser {
	
	private Vocabulary rssVocabulary;
	
	public FeedListParser(){
		this.rssVocabulary = new Vocabulary("RSS", null);
	}
	
	public List<Feed> readFeedList(){
		List<Feed> feeds = new ArrayList<Feed>();
		BufferedReader bReader;
		try {
			bReader = new BufferedReader(new FileReader("data/feeds.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return feeds;
		}
		String line;
		int lineCount = 0;
		try {
			while((line=bReader.readLine())!=null){
				lineCount++;
				if(line.trim().startsWith("#")){
					continue; //This is a comment line;
				}
				if(line.trim().length()==0){
					continue; //Empty line;
				}
				String[] splitArray = line.split(",");
				if(splitArray.length!=3 && splitArray.length!=6){
					System.err.println("Error while parsing line #" + lineCount + " of the feed list!");
					continue; //Malformed line
				}
				String feedUrl = splitArray[0].trim();
				Term category = new Term(splitArray[1].trim(), rssVocabulary);
				Language language = Language.valueOf(splitArray[2].trim());
				if(splitArray.length==3){
					Feed currFeed = new Feed(feedUrl, category, language);
					feeds.add(currFeed);
				} else {
					try{
						double lat = Double.parseDouble(splitArray[3].trim());
						double lon = Double.parseDouble(splitArray[4].trim());
						int radius = Integer.parseInt(splitArray[5].trim());
						Feed currFeed = new Feed(feedUrl, category, language, lat, lon, radius);
						feeds.add(currFeed);
					} catch (NumberFormatException e) {
						System.err.println("Error while parsing line #" + lineCount + " of the feed list!");
						continue; //Malformed line
					}
				}
			}
			bReader.close();			
		} catch (IOException e) {
			System.err.println("Error while reading the feed list file!");
			e.printStackTrace();
		}
		return feeds;
	}
}