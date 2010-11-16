package fom.model.dao.localdb;

import java.sql.Connection;

import fom.model.Vocabulary;
import fom.model.dao.VocabularyDAO;

public class LocalDBVocabularyDAO implements VocabularyDAO {

	Connection conn;
	
	public LocalDBVocabularyDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public long create(Vocabulary vocabulary) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vocabulary retrieve(long vocabularyId) {
		// TODO Auto-generated method stub
		return null;
	}

}
