package fom.model.dao;

public interface DAOFactory {
	
	public ClusterDAO getClusterDAO();
	public MediaDAO getMediaDAO();
	public PostDAO getPostDAO();
	public QueryDAO getQueryDAO();
	
}
