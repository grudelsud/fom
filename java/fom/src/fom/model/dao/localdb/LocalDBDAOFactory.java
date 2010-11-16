package fom.model.dao.localdb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import fom.model.dao.ClusterDAO;
import fom.model.dao.DAOFactory;
import fom.model.dao.MediaDAO;
import fom.model.dao.PlaceDAO;
import fom.model.dao.PostDAO;
import fom.model.dao.QueryDAO;
import fom.model.dao.TermDAO;
import fom.model.dao.VocabularyDAO;

public class LocalDBDAOFactory extends DAOFactory {

	Connection conn;
	
	public LocalDBDAOFactory(){
		this.conn = getConnection();
	}
	
	private Connection getConnection() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("properties/properties.properties"));
			String connURL = prop.getProperty("LocalDBConnectionURL");
			String username = prop.getProperty("LocalDBUser");
			String pass = prop.getProperty("localDBPass");
			return DriverManager.getConnection(connURL, username, pass);			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ClusterDAO getClusterDAO() {
		return new LocalDBClusterDAO(conn);
	}

	@Override
	public MediaDAO getMediaDAO() {
		return new LocalDBMediaDAO(conn);
	}

	@Override
	public PostDAO getPostDAO() {
		return new LocalDBPostDAO(conn);
	}

	@Override
	public QueryDAO getQueryDAO() {
		return new LocalDBQueryDAO(conn);
	}

	@Override
	public PlaceDAO getPlaceDAO() {
		return new LocalDBPlaceDAO(conn);
	}

	@Override
	public TermDAO getTermDAO() {
		return new LocalDBTermDAO(conn);
	}

	@Override
	public VocabularyDAO getVocabularyDAO() {
		return new LocalDBVocabularyDAO(conn);
	}

}
