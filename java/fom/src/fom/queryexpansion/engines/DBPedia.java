package fom.queryexpansion.engines;

import java.util.ArrayList;
import java.util.List;

public class DBPedia implements ExpansionEngine {

	@Override
	public List<String> expandQuery(String query) {
		List<String> expandedQuery = new ArrayList<String>();
		
		expandedQuery.add(query);
		
		return expandedQuery;
	}

}
