package fom.application;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class Main {

	public static void main(String[] args) {
				
		String queryString = "shepherds bush empire";
		
		String expEngineName = "wikiminer";

		String timeGranularity = "day";
		
		List<String> sourceNames = new ArrayList<String>();
		sourceNames.add("twitter");

		DateTime startTime = new DateTime().minusDays(8);
		DateTime endTime = new DateTime().minusDays(0);
		
		long userId = 1;
		
		QueryHandler qHandler = new QueryHandler(userId, queryString, expEngineName, sourceNames, startTime, endTime, timeGranularity);
		
		qHandler.executeQuery();
	}
}
