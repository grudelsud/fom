package fom.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author  Federico Frappi
 */
public class StringOperations {

	private static StopwordChecker stopwords = new StopwordChecker();

	public static String hashtagify(String input){
		String result = new String("#");
		String[] tokens = input.split("\\s");
		for(int i=0; i<tokens.length; i++){
				result = result.concat(tokens[i]);
		}
		return result;
	}
	
	public static List<String> extractHashtags(String tweetText){
		List<String> hashtags = new ArrayList<String>();
		String regex = "#\\w+";
		Pattern p = Pattern.compile(regex); 
		Matcher m = p.matcher(tweetText);
		while(m.find()) {  
			String htag = m.group();
		//	System.out.println("Found hashtag: " + htag);
			hashtags.add(htag);
		}
		return hashtags;
	}
	
	public static String removeStopwords(String input){
		String result = new String("");
		String[] tokens = input.split("\\s");
		for(int i=0; i<tokens.length; i++){
			if(!stopwords.isStopword(tokens[i])){
				result = result.concat(tokens[i]);
				result = result.concat(" ");
			}
		}
		if(result.length()!=0){
			result = result.substring(0, result.length()-1);
		}
		return result;
	}
	
	public static String removeURLfromString(String originalText){
		String result = originalText;
		String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
		Pattern p = Pattern.compile(regex); 
		Matcher m = p.matcher(originalText);
		while(m.find()) {
			String url = m.group();  
			if (url.startsWith("(") ){
				url = url.substring(1, url.length());
			}
			if (url.endsWith(")")){
				url = url.substring(0, url.length()-1);
			}
			result = result.replace(url, "");
		}
		return result;
	}
	
	public static List<String> extractURLs(String originalText){
		List<String> urls = new ArrayList<String>();
		String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
		Pattern p = Pattern.compile(regex); 
		Matcher m = p.matcher(originalText);
		while(m.find()) {
			String url = m.group();
			if(url.contains("twitpic") || url.contains("tweetphoto")){
				if (url.startsWith("(") ){
					url = url.substring(1, url.length());
				}
				if (url.endsWith(")")){
					url = url.substring(0, url.length()-1);
				}
				urls.add(url);
			}
		}
		return urls;
	}

	public static String makeSqlList(List<?> objects) {
		String result = "(";
		for(int i=0; i<objects.size()-1;i++){
			result=result.concat("'" + objects.get(i).toString() +"'" + ", ");
		}
		if(objects.size()>0){
			result=result.concat("'" + objects.get(objects.size()-1).toString() + "'");
		}
		result = result.concat(")");
		return result;
	}
	
	
	public static String removeHash(String input){
		if(input.startsWith("#")){
			return input.substring(1);
		}else return input;
	}
	
	public static String concatStrings(List<String> strings){
		String result = "";
		for(String token : strings){
			result += token;
			result += " ";
		}
		return result.trim();
	}
	
	
}
