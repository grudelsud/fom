package fom.model;

public class Term {
	private String name;
	private String url;
	private Term syn;
	private Term parent;
	private Vocabulary vocabulary;
		
	public Term(String name, String url, Term syn, Term parent, Vocabulary vocabulary) {
		super();
		this.name = name;
		this.url = url;
		this.syn = syn;
		this.parent = parent;
		this.vocabulary = vocabulary;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Term getSyn() {
		return syn;
	}
	public void setSyn(Term syn) {
		this.syn = syn;
	}
	public Term getParent() {
		return parent;
	}
	public void setParent(Term parent) {
		this.parent = parent;
	}
	public Vocabulary getVocabulary() {
		return vocabulary;
	}
	public void setVocabulary(Vocabulary vocabulary) {
		this.vocabulary = vocabulary;
	}
}
