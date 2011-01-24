package fom.model.dao.rpc;

import fom.model.dao.interfaces.ClusterDAO;
import fom.model.dao.interfaces.DAOFactory;
import fom.model.dao.interfaces.LinkDAO;
import fom.model.dao.interfaces.MediaDAO;
import fom.model.dao.interfaces.PlaceDAO;
import fom.model.dao.interfaces.PostDAO;
import fom.model.dao.interfaces.QueryDAO;
import fom.model.dao.interfaces.TermDAO;
import fom.model.dao.interfaces.VocabularyDAO;

public class RPCDAOFactory extends DAOFactory {

	@Override
	public ClusterDAO getClusterDAO() {
		return new RPCClusterDAO();
	}

	@Override
	public MediaDAO getMediaDAO() {
		return new RPCMediaDAO();
	}

	@Override
	public PostDAO getPostDAO() {
		return new RPCPostDAO();
	}

	@Override
	public QueryDAO getQueryDAO() {
		return new RPCQueryDAO();
	}

	@Override
	public PlaceDAO getPlaceDAO() {
		return new RPCPlaceDAO();
	}

	@Override
	public TermDAO getTermDAO() {
		return new RPCTermDAO();
	}

	@Override
	public VocabularyDAO getVocabularyDAO() {
		return new RPCVocabularyDAO();
	}

	@Override
	public LinkDAO getLinkDAO() {
		return new RPCLinkDAO();
	}
	
	

}
