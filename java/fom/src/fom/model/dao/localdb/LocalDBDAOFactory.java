package fom.model.dao.localdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import fom.model.dao.interfaces.ClusterDAO;
import fom.model.dao.interfaces.DAOFactory;
import fom.model.dao.interfaces.MediaDAO;
import fom.model.dao.interfaces.PlaceDAO;
import fom.model.dao.interfaces.PostDAO;
import fom.model.dao.interfaces.QueryDAO;
import fom.model.dao.interfaces.TermDAO;
import fom.model.dao.interfaces.UserDAO;
import fom.model.dao.interfaces.VocabularyDAO;
import fom.properties.PropertyHandler;

public class LocalDBDAOFactory extends DAOFactory {

	Connection conn;
	
	public LocalDBDAOFactory(){
		this.conn = getConnection();
	}
	
	private Connection getConnection() {
		Properties prop = PropertyHandler.getInstance().getProperties();
		try {
			String connURL = prop.getProperty("LocalDBConnectionURL");
			String username = prop.getProperty("LocalDBUser");
			String pass = prop.getProperty("localDBPass");
			return DriverManager.getConnection(connURL, username, pass);
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

	@Override
	public UserDAO getUserDAO() {
		return new LocalDBUserDAO(conn);
	}

}
