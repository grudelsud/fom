package fom.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SemanticCluster extends Cluster {
	
	private List<Term> terms;	
	
	public SemanticCluster() {
		super();
		this.terms = new ArrayList<Term>();
	}

	@Override
	public Map<String, String> getMeta() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Term> getClusterTerms() {
		return terms;
	}

}
