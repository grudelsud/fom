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
import fom.model.dao.interfaces.VocabularyDAO;
import fom.properties.PropertyHandler;

public class LocalDBDAOFactory extends DAOFactory {

	LocalDBClusterDAO clusterDAO;
	LocalDBMediaDAO mediaDAO;
	LocalDBPlaceDAO placeDAO;
	LocalDBPostDAO postDAO;
	LocalDBQueryDAO queryDAO;
	LocalDBTermDAO termDAO;
	LocalDBVocabularyDAO vocabularyDAO;
	
	public LocalDBDAOFactory(){
		Connection conn = getConnection();
		clusterDAO = new LocalDBClusterDAO(conn);
		mediaDAO = new LocalDBMediaDAO(conn);
		placeDAO = new LocalDBPlaceDAO(conn);
		postDAO = new LocalDBPostDAO(conn);
		queryDAO = new LocalDBQueryDAO(conn);
		termDAO = new LocalDBTermDAO(conn);
		vocabularyDAO = new LocalDBVocabularyDAO(conn);
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
		return clusterDAO;
	}

	@Override
	public MediaDAO getMediaDAO() {
		return mediaDAO;
	}

	@Override
	public PostDAO getPostDAO() {
		return postDAO;
	}

	@Override
	public QueryDAO getQueryDAO() {
		return queryDAO;
	}

	@Override
	public PlaceDAO getPlaceDAO() {
		return placeDAO;
	}

	@Override
	public TermDAO getTermDAO() {
		return termDAO;
	}

	@Override
	public VocabularyDAO getVocabularyDAO() {
		return vocabularyDAO;
	}

}
