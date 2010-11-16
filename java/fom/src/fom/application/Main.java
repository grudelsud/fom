package fom.application;

import fom.model.dao.ClusterDAO;
import fom.model.dao.DAOFactory;

public class Main {

	public static void main(String[] args) {
		DAOFactory fac = DAOFactory.getFactory();
		ClusterDAO clusDAO = fac.getClusterDAO();
		clusDAO.retrieve(0);
	}
}
