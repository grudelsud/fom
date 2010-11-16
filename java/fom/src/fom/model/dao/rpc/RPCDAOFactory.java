package fom.model.dao.rpc;

import fom.model.dao.ClusterDAO;
import fom.model.dao.DAOFactory;
import fom.model.dao.MediaDAO;
import fom.model.dao.PlaceDAO;
import fom.model.dao.PostDAO;
import fom.model.dao.QueryDAO;
import fom.model.dao.TermDAO;
import fom.model.dao.VocabularyDAO;

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

}
