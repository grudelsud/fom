package fom.model.dao.localdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fom.model.Term;
import fom.model.Vocabulary;
import fom.model.dao.interfaces.DAOFactory;
import fom.model.dao.interfaces.TermDAO;

public class LocalDBTermDAO implements TermDAO {
	
	private PreparedStatement stm;
	private PreparedStatement getTermByNameStm;
	private PreparedStatement getTermByIdStm;
	

	public LocalDBTermDAO(Connection conn) {
		try {
			stm  = conn.prepareStatement("INSERT INTO fom_term(name, id_termsyn, id_termparent, id_vocabulary, uri) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			getTermByNameStm = conn.prepareStatement("SELECT id_term FROM fom_term WHERE name LIKE ? AND uri LIKE ?");
			getTermByIdStm  = conn.prepareStatement("SELECT * FROM fom_term WHERE id_term=?");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public long create(Term term) {
		try {		
			if(term.getId()!=0){
				return term.getId();
			}
			getTermByNameStm.setString(1, term.getName());
			getTermByNameStm.setString(2, term.getUrl());
			ResultSet getTermRes = getTermByNameStm.executeQuery();
			if(getTermRes.next()){
				term.setId(getTermRes.getLong("id_term"));
				return term.getId();
			}
		
			stm.setString(1, term.getName());
			if(term.getSyn()!=null){
				stm.setLong(2, DAOFactory.getFactory().getTermDAO().create(term.getSyn()));				
			}else{
				stm.setNull(2, java.sql.Types.BIGINT);
			}
			if(term.getParent()!=null){
				stm.setLong(3, DAOFactory.getFactory().getTermDAO().create(term.getParent()));				
			}else{
				stm.setNull(3, java.sql.Types.BIGINT);
			}
			stm.setLong(4, DAOFactory.getFactory().getVocabularyDAO().create(term.getVocabulary()));
			stm.setString(5, term.getUrl());
			
			stm.executeUpdate();
			ResultSet generatedKeys = stm.getGeneratedKeys();
			if(generatedKeys.next()){
				term.setId(generatedKeys.getLong(1));
			} else {
				System.err.println("Error creating term");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return term.getId();
	}

	@Override
	public Term retrieve(long termId) {
		Term term = null;
		try{
			getTermByIdStm.setLong(1, termId);
			ResultSet res = getTermByIdStm.executeQuery();
			while(res.next()){
				String name = res.getString("name");
				String url = res.getString("uri");
				Term syn = DAOFactory.getFactory().getTermDAO().retrieve(res.getLong("id_termsyn"));
				Term parent = DAOFactory.getFactory().getTermDAO().retrieve(res.getLong("id_termparent"));
				Vocabulary vocabulary = DAOFactory.getFactory().getVocabularyDAO().retrieve(res.getLong("id_vocabulary"));
				term = new Term(name, url, syn, parent, vocabulary);
				term.setId(termId);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return term;
	}

}
