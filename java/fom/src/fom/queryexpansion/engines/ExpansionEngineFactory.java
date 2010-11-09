package fom.queryexpansion.engines;

public class ExpansionEngineFactory {
	
	public static ExpansionEngine getExpansionEngine(String expEngineName){
		if(expEngineName.equalsIgnoreCase("dbpedia")){
			return new DBPedia();
		}
		else if(expEngineName.equalsIgnoreCase("wikiminer")){
			return new Wikiminer();
		}
		return null; //TODO Aggiungere eccezione?
	}
}
