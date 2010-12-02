package fom.application;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

public class Main {

	public static void main(String[] args){
				
		long userId = 1;
		String queryString = "concert";		
		String expEngineName = "wikiminer";
		
		double nearLat = 40.751939;
		double nearLon = -73.981934;
		int radius = 10;
		
		String timeGranularity = "day";
		String geoGranularity = "neighborhood";
		
		List<String> sourceNames = new ArrayList<String>();
		sourceNames.add("twitter");

		DateTime startTime = new DateTime().minusDays(5);
		DateTime endTime = new DateTime().minusDays(0);
		
		QueryHandler qHandler = new QueryHandler(userId, queryString, expEngineName, sourceNames, startTime, endTime, timeGranularity, geoGranularity, nearLat, nearLon, radius);
		
		qHandler.executeQuery();
	}
}
