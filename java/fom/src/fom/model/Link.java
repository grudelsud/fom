package fom.model;

public class Link {
	private long id;
	private String url;
	private String content;
	
	public Link(String url, String content){
		this.id = 0;
		this.setUrl(url);
		this.setContent(content);
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
	
}
