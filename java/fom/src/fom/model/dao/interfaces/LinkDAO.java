package fom.model.dao.interfaces;

import fom.model.Link;

public interface LinkDAO {

	public long create(Link link);
	public Link retrieve(long linkId);
	
}
