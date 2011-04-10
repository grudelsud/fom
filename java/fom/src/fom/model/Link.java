package fom.model;

import java.util.Map;

import fom.langidentification.LanguageIdentifier.Language;

public class Link {
	private long id;
	private String url;
	private String content;
	private Language lang;
	private Map<String, String> meta;
	
	public Link(String url, String content, Language lang, Map<String, String> meta){
		this.id = 0;
		this.url = url;
		this.content = content;
		this.lang = lang;
		this.meta = meta;
	}

	public long getId(){
		return id;
	}

	public String getUrl() {
		return url;
	}
	
	public String getContent() {
		return content;
	}
	
	public Language getLanguage(){
		return lang;
	}

	public Map<String, String> getMeta() {
		return meta;
	}

	public void setId(long id) {
		this.id = id;
	}
}
