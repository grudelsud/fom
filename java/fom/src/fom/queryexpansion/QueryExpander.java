package fom.queryexpansion;

import java.util.List;

import fom.queryexpansion.engines.ExpansionEngineFactory;
import fom.queryexpansion.engines.ExpansionEngine;

public class QueryExpander {
	ExpansionEngine expEngine;
	
	public QueryExpander(String expEngineName){
		this.expEngine = ExpansionEngineFactory.getExpansionEngine(expEngineName);
	}
	
	public List<String> expandQuery(String query){
		return expEngine.expandQuery(query);
	}
	
	public void setExpansionEngine(String expEngineName){
		this.expEngine = ExpansionEngineFactory.getExpansionEngine(expEngineName);
	}
}
