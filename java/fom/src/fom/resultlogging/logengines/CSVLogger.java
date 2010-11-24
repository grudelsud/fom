package fom.resultlogging.logengines;

import fom.model.GeoCluster;
import fom.model.Query;
import fom.model.SemanticCluster;
import fom.model.Term;
import fom.model.TimeCluster;

public class CSVLogger implements LogEngine{
	
	private String csvLog;
	private int timeClusterCount;
	private TimeCluster lastTimeCluster;
	private int geoClusterCount;
	private GeoCluster lastGeoCluster;
	private int semClusterCount;
	private SemanticCluster lastSemCluster;
	private boolean finalized=false;
	
	@Override
	public void startLogging(Query query) {
		csvLog = new String();
		csvLog = csvLog.concat("timeclusterX, startT, endT, timeClusterSize, geoclusterY, latM, latV, lonM, lonV, geoclusterSize, semclusterSize, \"list of terms enclosed in doublequotes and separated with semicolumns\"\n");
	}

	@Override
	public void addTimeCluster(TimeCluster timeCluster) {
		if(timeCluster!=lastTimeCluster){
			lastTimeCluster = timeCluster;
			timeClusterCount++;
			geoClusterCount=0;
		}
		csvLog = csvLog.concat("tc" + timeClusterCount + ", ");
		csvLog = csvLog.concat(timeCluster.getStartTime().toString("yyyy-MM-dd HH:mm:ss") + ", ");
		csvLog = csvLog.concat(timeCluster.getEndTime().toString("yyyy-MM-dd HH:mm:ss") + ", ");
		csvLog = csvLog.concat(timeCluster.getPosts().size() + ", ");
	}

	@Override
	public void addGeoCluster(GeoCluster geoCluster) {
		if(lastGeoCluster!=geoCluster){
			lastGeoCluster = geoCluster;
			geoClusterCount++;
			semClusterCount=0;
			if(geoClusterCount!=1){
				addTimeCluster(lastTimeCluster);
			}
		}
		csvLog = csvLog.concat("gc" + geoClusterCount + ", ");
		csvLog = csvLog.concat(geoCluster.getMeanLat() + ", ");
		csvLog = csvLog.concat(geoCluster.getStdDevLat() + ", ");
		csvLog = csvLog.concat(geoCluster.getMeanLon() + ", ");
		csvLog = csvLog.concat(geoCluster.getStdDevLon() + ", ");
		csvLog = csvLog.concat(geoCluster.getPosts().size() + ", ");
	}

	@Override
	public void addSemCluster(SemanticCluster semCluster) {
		if(lastSemCluster!=semCluster){
			lastSemCluster = semCluster;
			semClusterCount++;
			if(semClusterCount!=1){
				addTimeCluster(lastTimeCluster);
				addGeoCluster(lastGeoCluster);
			}
		}
		csvLog = csvLog.concat(semCluster.getPosts().size() + ", ");
		csvLog = csvLog.concat("\"");
		for(Term t: semCluster.getTerms()){
			csvLog = csvLog.concat(t.getName()+";");
		}
		csvLog = csvLog.concat("\"\n");
	}

	@Override
	public void endLog() {
		finalized = true;
	}

	@Override
	public String getLog() {
		if(finalized)
			return csvLog; //TODO: throw exception if not finalized
		return "";
	}

}
