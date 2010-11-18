package fom.model.dao.interfaces;

import fom.model.Query;

public interface QueryDAO {
	public long create(Query query);
	public Query retrieve(long queryId);
}
