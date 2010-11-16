package fom.model.dao.localdb;

import java.sql.Connection;

import fom.model.Query;
import fom.model.dao.QueryDAO;

public class LocalDBQueryDAO implements QueryDAO {

	Connection conn;
	
	public LocalDBQueryDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public long create(Query query) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Query retrieve(long queryId) {
		// TODO Auto-generated method stub
		return null;
	}



}
