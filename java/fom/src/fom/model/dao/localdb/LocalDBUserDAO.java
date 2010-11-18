package fom.model.dao.localdb;

import java.sql.Connection;

import fom.model.User;
import fom.model.dao.interfaces.UserDAO;

public class LocalDBUserDAO implements UserDAO {

	public LocalDBUserDAO(Connection conn) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long create(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public User retrieve(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
