package fom.model;

import java.util.ArrayList;
import java.util.List;

public class Vocabulary {
	private String desc;
	private String owl;
	private List<Term> terms;
	
	public Vocabulary(String desc, String owl){
		this.desc = desc;
		this.owl = owl;
		this.terms = new ArrayList<Term>();
	}

	public String getDesc() {
		return desc;
	}

	public String getOwl() {
		return owl;
	}

	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}

	public List<Term> getTerms() {
		return terms;
	}
	
	public void addTerm(Term theTerm){
		this.terms.add(theTerm);
	}

}
