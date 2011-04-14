package fom.resultlogging.logengines;

import fom.model.GeoCluster;
import fom.model.Query;
import fom.model.TopicCluster;
import fom.model.TimeCluster;

public interface LogEngine {
	
	public void startLogging(Query query);
	public void addTimeCluster(TimeCluster timeCluster);
	public void addGeoCluster(GeoCluster geoCluster);
	public void addSemCluster(TopicCluster semCluster);
	public void endLog();
	
	public String getLog();
}
