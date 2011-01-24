package fom.resultlogging.logengines;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import fom.model.GeoCluster;
import fom.model.Query;
import fom.model.SemanticCluster;
import fom.model.Term;
import fom.model.TimeCluster;
import fom.properties.PropertyHandler;

public class RPCRemoteLogger implements LogEngine{
	
	private Long queryId;
	private int timeClusterCount;
	private TimeCluster lastTimeCluster;
	private int geoClusterCount;
	private GeoCluster lastGeoCluster;
	private int semClusterCount;
	private SemanticCluster lastSemCluster;
	private XmlRpcClient xmlRpcClient;
	
	public RPCRemoteLogger(){
		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL(PropertyHandler.getStringProperty("RPCEndPointServer")));
			config.setEnabledForExtensions(true);
			xmlRpcClient = new XmlRpcClient();
			xmlRpcClient.setConfig(config);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void startLogging(Query query) {
		String queryString = new String();
		for(Term term: query.getTerms()){
			queryString = queryString.concat(term.getName() + ", ");
		}
		queryString = queryString.substring(0, queryString.length()-2);
		String startTime = query.getStartTime().toString("yyyy-MM-dd HH:mm:ss");
		String endTime = query.getEndTime().toString("yyyy-MM-dd HH:mm:ss");
		String where = new String("(" + query.getLat() + ", " + query.getLon() + ")");
		String granularity = query.getTimeGranularity();
		String source = "twitter";
		
		Object[] params = new Object[]{queryString, startTime, endTime, where, granularity, source};
		try {
			queryId = Long.parseLong(((HashMap<?, ?>)(xmlRpcClient.execute("query_store", params))).get("response").toString());
		} catch (XmlRpcException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		String tcStartTime = lastTimeCluster.getStartTime().toString("yyyy-MM-dd HH:mm:ss");
		String tcEndTime = lastTimeCluster.getEndTime().toString("yyyy-MM-dd HH:mm:ss");
		Integer tcSize = lastTimeCluster.getPosts().size();
		String gcName = new String("gc" + geoClusterCount);
		Double gcMeanLat = lastGeoCluster.getMeanLat();
		Double gcVarLat = lastGeoCluster.getStdDevLat();
		Double gcMeanLon = lastGeoCluster.getMeanLon();
		Double gcVarLon = lastGeoCluster.getStdDevLon();
		Integer gcSize = lastGeoCluster.getPosts().size();
		Integer scSize = semCluster.getPosts().size();
		String scContent = new String();
		for(Term t: semCluster.getTerms()){
			scContent = scContent.concat(t.getName()+";");
		}
		
		Object[] params = new Object[]{queryId.toString(), tcStartTime, tcEndTime, tcSize.toString(), gcName, gcMeanLat, gcVarLat, gcMeanLon, gcVarLon, gcSize.toString(), scSize.toString(), scContent};
		try {
			xmlRpcClient.execute("cluster_store", params);
		} catch (XmlRpcException e) {
			e.printStackTrace();
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
