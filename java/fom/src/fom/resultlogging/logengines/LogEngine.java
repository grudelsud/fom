package fom.resultlogging.logengines;

import fom.model.GeoCluster;
import fom.model.Query;
import fom.model.SemanticCluster;
import fom.model.TimeCluster;

public interface LogEngine {
	
	public void startLogging(Query query);
	public void addTimeCluster(TimeCluster timeCluster);
	public void addGeoCluster(GeoCluster geoCluster);
	public void addSemCluster(SemanticCluster semCluster);
	public void endLog();
	
	public String getLog();
}
