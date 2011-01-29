package fom.model;

public class Term {

	private long id;
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

	public String getUrl() {
		return url;
	}

	public Term getSyn() {
		return syn;
	}

	public Term getParent() {
		return parent;
	}

	public Vocabulary getVocabulary() {
		return vocabulary;
	}

	public long getId(){
		return id;
	}
	
	public void setId(long termId) {
		this.id = termId;		
	}
	
	@Override
	public String toString() {
		return name;
	}
}
