package fom.resultlogging.logengines;

import fom.model.GeoCluster;
import fom.model.Query;
import fom.model.SemanticCluster;
import fom.model.TimeCluster;

public class ConsoleLogger implements LogEngine {

	@Override
	public void startLogging(Query query) {
		System.out.println(query.toString());
	}

	@Override
	public void addTimeCluster(TimeCluster timeCluster) {
		System.out.println("+) Found TimeCluster");
	}

	@Override
	public void addGeoCluster(GeoCluster geoCluster) {
		System.out.println("\t-) Found GeoCluster");
	}

	@Override
	public void addSemCluster(SemanticCluster semCluster) {
		System.out.println("\t\t-) Found SemCluster");
	}

	@Override
	public void endLog() {
		System.out.println("---");
	}

	@Override
	public String getLog() {
		return null;
	}

}
