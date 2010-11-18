package fom.model.dao.interfaces;

import fom.model.dao.localdb.LocalDBDAOFactory;
import fom.model.dao.rpc.RPCDAOFactory;
import fom.properties.PropertyHandler;

public abstract class DAOFactory {
	
	private static DAOFactory factoryInstance;
	
	public static DAOFactory getFactory(){
		if(factoryInstance==null){
			String dataSourceProp = PropertyHandler.getInstance().getProperties().getProperty("DataSource");
			if(dataSourceProp.equalsIgnoreCase("local")){					
				System.out.println("Created Local DAO");
				factoryInstance = new LocalDBDAOFactory();
			}
			else if(dataSourceProp.equalsIgnoreCase("rpc")){
				System.out.println("Created RPC DAO");
				factoryInstance = new RPCDAOFactory();
			}
			else{
				System.err.println("Something is wrong with the property file! Created Local DAO");
				factoryInstance = new LocalDBDAOFactory(); //Defaults to local
			}
		}
		return factoryInstance;
	}
	
	public abstract ClusterDAO getClusterDAO();
	public abstract MediaDAO getMediaDAO();
	public abstract PlaceDAO getPlaceDAO();
	public abstract PostDAO getPostDAO();
	public abstract QueryDAO getQueryDAO();
	public abstract TermDAO getTermDAO();
	public abstract VocabularyDAO getVocabularyDAO();
	public abstract UserDAO getUserDAO();
	
}
