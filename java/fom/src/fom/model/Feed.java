package fom.model;

import fom.langidentification.LanguageIdentifier.Language;

public class Feed {
	private long id;
	private String url;
	private Term category;
	private Language language;
	private boolean national;
	private double lat;
	private double lon;
	private int influenceRadiusKm;

	public Feed(String url, Term category, Language language) {
		super();
		this.id = 0;
		this.url = url;
		this.category = category;
		this.language = language;
		this.national = true;
	}
	
	public Feed(String url, Term category, Language language, double lat, double lon, int influenceRadiusKm) {
		super();
		this.id = 0;
		this.url = url;
		this.category = category;
		this.language = language;
		this.lat = lat;
		this.lon = lon;
		this.influenceRadiusKm = influenceRadiusKm;
		this.national = false;
	}
	
	public void setId(long theId){
		this.id = theId;
	}
	
	public long getId(){
		return id;
	}

	public String getUrl() {
		return url;
	}

	public Term getCategory() {
		return category;
	}

	public Language getLanguage() {
		return language;
	}

	public boolean isNational() {
		return national;
	}

	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public int getInfluenceRadiusKm() {
		return influenceRadiusKm;
	}
	
	@Override
	public String toString() {				
		String str = new String(this.url + " - " + this.category + " - " + this.language);
		if(national){
			str = str.concat(" - Influence: National");
		} else {
			str = str.concat(" - Influence: " + this.influenceRadiusKm + "Km around (" + this.lat + "," + this.lon + ")");				
		}
		return str;
	}
}


