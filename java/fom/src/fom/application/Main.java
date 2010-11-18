package fom.application;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class Main {

	public static void main(String[] args) {
		String queryString = "#music";
		
		String expEngineName = "wikiminer";

		List<String> sourceNames = new ArrayList<String>();
		sourceNames.add("twitter");

		DateTime startTime = new DateTime().minusDays(1);
		DateTime endTime = new DateTime();
		
		long userId = 1;
		
		QueryHandler qHandler = new QueryHandler(userId, queryString, expEngineName, sourceNames, startTime, endTime);
		
		qHandler.executeQuery();
	}
}
