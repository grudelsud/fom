package fom.model.dao.interfaces;

import fom.model.Vocabulary;

public interface VocabularyDAO {
	public long create(Vocabulary vocabulary);
	public Vocabulary retrieve(long vocabularyId);
}
