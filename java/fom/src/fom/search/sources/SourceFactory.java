package fom.search.sources;

public class SourceFactory {
	
	public static enum SourceType{
		TWITTER, TEAMLIFE, LOCALDB
	}
	
	public static Source getSource(SourceType sourceType){
		switch (sourceType) {
		case TWITTER:
			return new Twitter();
		case TEAMLIFE:
			return new Teamlife();
		case LOCALDB:
			return new LocalDBSource();
		default:
			return null;
		}
	}
	
}
