package fom.search.sources;

public class SourceFactory {
	
	public static Source getSource(String sourceName){
		if(sourceName.equalsIgnoreCase("twitter")){
			return new Twitter();
		}
		else if(sourceName.equalsIgnoreCase("teamlife")){
			return new Teamlife();
		}
		return null; //TODO aggiungere eccezione?
	}
	
}
