package fom.model.dao.localdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fom.model.Vocabulary;
import fom.model.dao.interfaces.VocabularyDAO;

public class LocalDBVocabularyDAO implements VocabularyDAO {

	
	private PreparedStatement stm;
	private PreparedStatement getVocByNameStm;
	private PreparedStatement getVocByIdStm;


	public LocalDBVocabularyDAO(Connection conn) {
		try {
			stm  = conn.prepareStatement("INSERT INTO fom_vocabulary(name, owl) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
			getVocByNameStm = conn.prepareStatement("SELECT id_vocabulary FROM fom_vocabulary WHERE name LIKE ?");
			getVocByIdStm  = conn.prepareStatement("SELECT * FROM fom_vocabulary WHERE id_vocabulary=?");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public long create(Vocabulary vocabulary) {
		try {		
			if(vocabulary.getId()!=0){
				return vocabulary.getId();
			}
			getVocByNameStm.setString(1, vocabulary.getDesc()); 			//TODO: controllare l'owl????
			ResultSet getVocRes = getVocByNameStm.executeQuery();
			if(getVocRes.next()){
				vocabulary.setId(getVocRes.getLong("id_vocabulary"));
				return vocabulary.getId();
			}
		
			stm.setString(1, vocabulary.getDesc());
			stm.setString(2, vocabulary.getOwl());
			
			stm.executeUpdate();
			ResultSet generatedKeys = stm.getGeneratedKeys();
			if(generatedKeys.next()){
				vocabulary.setId(generatedKeys.getLong(1));
			} else {
				System.err.println("Error creating vocabulary");
			}
		}
		catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return vocabulary.getId();
	}

	@Override
	public Vocabulary retrieve(long vocabularyId) {
		Vocabulary voc = null;
		try{
			getVocByIdStm.setLong(1, vocabularyId);
			ResultSet res = getVocByIdStm.executeQuery();
			while(res.next()){
				String desc = res.getString("name");
				String owl = res.getString("owl");
				voc = new Vocabulary(desc, owl);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return voc;
	}

}
