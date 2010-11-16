package fom.model;

public class TermRelation {
	private Term term1;
	private Term term2;
	private String relType;
	
	public TermRelation(Term term1, Term term2, String relType) {
		super();
		this.term1 = term1;
		this.term2 = term2;
		this.relType = relType;
	}
	public Term getTerm1() {
		return term1;
	}
	public Term getTerm2() {
		return term2;
	}
	public String getRelType() {
		return relType;
	}
	
	
}
