package fom.model.dao.localdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fom.model.Link;
import fom.model.dao.interfaces.LinkDAO;

public class LocalDBLinkDAO implements LinkDAO {

	private PreparedStatement checkAlreadySavedStm;
	private PreparedStatement saveLinkStm;
	private PreparedStatement retrieveLinkStm;
	
	public LocalDBLinkDAO(Connection conn) {
		try {
			this.checkAlreadySavedStm = conn.prepareStatement("SELECT id_link FROM fom_link WHERE uri=?");
			this.saveLinkStm = conn.prepareStatement("INSERT INTO fom_link(uri, text) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
			this.retrieveLinkStm = conn.prepareStatement("SELECT * FROM fom_link WHERE id_link = ?");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public long create(Link link) {
		if(link.getId()!=0){
			return link.getId();
		}
		try {
			checkAlreadySavedStm.setString(1, link.getUrl());
			ResultSet res = checkAlreadySavedStm.executeQuery();
			if(res.next()){
				link.setId(res.getLong("id_link"));
				return link.getId();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Link not stored in the DB yet
		try {
			saveLinkStm.setString(1, link.getUrl());
			saveLinkStm.setString(2, link.getContent());
			
			saveLinkStm.executeUpdate();
			ResultSet key = saveLinkStm.getGeneratedKeys();
			if(key.next()){
				link.setId(key.getLong(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return link.getId();		
	}

	@Override
	public Link retrieve(long linkId) {
		try {
			retrieveLinkStm.setLong(1, linkId);
			ResultSet res = retrieveLinkStm.executeQuery();
			if(res.next()){
				Link link = new Link(res.getString("uri"), res.getString("text"));
				link.setId(res.getLong("id_link"));
				return link;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
