package fom.resultlogging.logengines;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fom.model.GeoCluster;
import fom.model.Post;
import fom.model.Query;
import fom.model.SemanticCluster;
import fom.model.Term;
import fom.model.TimeCluster;

public class FolderLogger implements LogEngine{
	
	private int timeClusterCount;
	private TimeCluster lastTimeCluster;
	private int geoClusterCount;
	private GeoCluster lastGeoCluster;
	private int semClusterCount;
	private SemanticCluster lastSemCluster;
	File queryBaseFolder;
	
	@Override
	public void startLogging(Query query) {
		queryBaseFolder = new File("results/" + System.currentTimeMillis() + "-" + query.getQuery() + "/" + "[" + query.getStartTime().toString("yyyy-MM-dd") + " " + query.getEndTime().toString("yyyy-MM-dd") +"] (" + query.getLat() + "," + query.getLon() +") [" + query.getTimeGranularity() + "," + query.getGeoGranularity() +"]" + "/");
		queryBaseFolder.mkdirs();
	}

	@Override
	public void addTimeCluster(TimeCluster timeCluster) {
		if(timeCluster!=lastTimeCluster){
			lastTimeCluster = timeCluster;
			timeClusterCount++;
			geoClusterCount=0;
		}
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
		File tcFolder = new File(queryBaseFolder.getPath() + "/tc" + timeClusterCount + " [" + lastTimeCluster.getStartTime().toString() + " " + lastTimeCluster.getEndTime().toString() + "]");
		tcFolder.mkdirs();
		
		File gcFolder = new File(tcFolder.getPath() + "/gc" + geoClusterCount + "[" + lastGeoCluster.getMeanLat() + "," + lastGeoCluster.getMeanLon() + "]");
		gcFolder.mkdirs();
		
		File scFolder = new File(gcFolder.getPath() + "/sc" + semClusterCount);
		scFolder.mkdirs();
		
		try {
		    BufferedWriter bwTerms = new BufferedWriter(new FileWriter(scFolder.getPath() + "/Cluster Terms.txt"));
		    for(Term t : semCluster.getTerms()){
		    	bwTerms.write(t.getName().toString() + "\n");
		    }
		    bwTerms.close();
		    
		    BufferedWriter bwPosts = new BufferedWriter(new FileWriter(scFolder.getPath() + "/Posts.txt"));
		    for(Post p : semCluster.getPosts()){
			    bwPosts.write(p.getContent() + "\n");
		    }
		    bwPosts.close();
		} catch (IOException e) {
		}
	}

	@Override
	public void endLog() {
	}

	@Override
	public String getLog() {
		return null;
	}

}
