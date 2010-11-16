package fom.model.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import fom.model.dao.localdb.LocalDBDAOFactory;
import fom.model.dao.rpc.RPCDAOFactory;

public abstract class DAOFactory {
	
	private static DAOFactory factoryInstance;
	
	public static DAOFactory getFactory(){
		if(factoryInstance==null){
			try{
				FileInputStream fos = new FileInputStream("properties/properties.properties");
				Properties prop = new Properties();
				prop.load(fos);
				if(prop.getProperty("DataSource").equalsIgnoreCase("local")){					
					System.out.println("Created Local DAO");
					factoryInstance = new LocalDBDAOFactory();
				}
				else if(prop.getProperty("DataSource").equalsIgnoreCase("rpc")){
					System.out.println("Created RPC DAO");
					factoryInstance = new RPCDAOFactory();
				}
				else{
					System.err.println("Something is wrong with the property file! Created Local DAO");
					factoryInstance = new LocalDBDAOFactory(); //Defaults to local
				}
				fos.close();
			}
			catch(IOException e){
				e.printStackTrace();
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
	
}
