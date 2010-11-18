package fom.application;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class Main {

	public static void main(String[] args) {
		String queryString = "great movie";
		
		String expEngineName = "wikiminer";

		List<String> sourceNames = new ArrayList<String>();
		sourceNames.add("twitter");

		DateTime startTime = new DateTime().minusDays(1);
		DateTime endTime = new DateTime();
		
		QueryHandler qHandler = new QueryHandler(queryString, expEngineName, sourceNames, startTime, endTime);
		
		qHandler.executeQuery();
	}
}
