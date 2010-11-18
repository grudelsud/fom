package fom.model;

import java.util.ArrayList;
import java.util.List;

public class Vocabulary {
	private String desc;
	private String owl;
	private List<Term> terms;
	private List<TermRelation> relations;
	
	public Vocabulary(String desc, String owl){
		this.desc = desc;
		this.owl = owl;
		this.terms = new ArrayList<Term>();
		this.relations = new ArrayList<TermRelation>();
	}

	public String getDesc() {
		return desc;
	}

	public String getOwl() {
		return owl;
	}

	public List<Term> getTerms() {
		return terms;
	}
	
	public void addTerm(Term theTerm){
		this.terms.add(theTerm);
	}

	public List<TermRelation> getRelations(){
		return relations;
	}
	
	public void addRelation(TermRelation termRel){
		relations.add(termRel);
	}
}
