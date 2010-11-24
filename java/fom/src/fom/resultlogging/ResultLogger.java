package fom.resultlogging;

import java.util.ArrayList;
import java.util.List;

import fom.model.GeoCluster;
import fom.model.Query;
import fom.model.SemanticCluster;
import fom.model.TimeCluster;
import fom.resultlogging.logengines.LogEngine;

public class ResultLogger {
	private List<LogEngine> logEngines;
	
	public ResultLogger(){
		this.logEngines = new ArrayList<LogEngine>();
	}
	
	public void addLogEngine(LogEngine engine) {
		logEngines.add(engine);
	}
	
	public void startLogging(Query query){
		for(LogEngine engine : logEngines){
			engine.startLogging(query);
		}
	}
	public void addTimeCluster(TimeCluster timeCluster){
		for(LogEngine engine : logEngines){
			engine.addTimeCluster(timeCluster);
		}
	}
	public void addGeoCluster(GeoCluster geoCluster){
		for(LogEngine engine : logEngines){
			engine.addGeoCluster(geoCluster);
		}
	}
	public void addSemCluster(SemanticCluster semCluster){
		for(LogEngine engine : logEngines){
			engine.addSemCluster(semCluster);
		}
	}
	public void endLog(){
		for(LogEngine engine : logEngines){
			engine.endLog();
		}
	}
	
	public List<String> getLogs(){
		List<String> logs = new ArrayList<String>();
		for(LogEngine engine : logEngines){
			logs.add(engine.getLog());
		}
		return logs;
	}
}
