package fom.model.dao.localdb;

import java.sql.Connection;

import fom.model.Term;
import fom.model.dao.interfaces.TermDAO;

public class LocalDBTermDAO implements TermDAO {

	Connection conn;
	
	public LocalDBTermDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public long create(Term term) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Term retrieve(long termId) {
		// TODO Auto-generated method stub
		return null;
	}

}
