package fom.model;

import fom.langidentification.LanguageIdentifier.Language;

public class Link {
	private long id;
	private String url;
	private String content;
	private Language lang;
	private String meta;
	
	public Link(String url, String content, Language lang, String meta){
		this.id = 0;
		this.url = url;
		this.content = content;
		this.lang = lang;
		this.meta = meta;
	}

	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
	
	public Language getLanguage(){
		return lang;
	}
	
	public void setLanguage(Language theLang){
		this.lang = theLang;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}

	public String getMeta() {
		return meta;
	}
}
