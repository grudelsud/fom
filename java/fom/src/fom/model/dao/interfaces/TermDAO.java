package fom.model.dao.interfaces;

import fom.model.Term;

public interface TermDAO {
	public long create(Term term);
	public Term retrieve(long termId);
}
