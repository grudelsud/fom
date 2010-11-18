package fom.model.dao.interfaces;

import fom.model.User;

public interface UserDAO {
	
	public long create(User user);
	public User retrieve(long userId);
}
