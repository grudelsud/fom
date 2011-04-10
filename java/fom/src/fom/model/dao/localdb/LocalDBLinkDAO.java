package fom.model.dao.localdb;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import fom.langidentification.LanguageIdentifier.Language;
import fom.model.Link;
import fom.model.dao.interfaces.LinkDAO;

public class LocalDBLinkDAO implements LinkDAO {

	private PreparedStatement checkAlreadySavedStm;
	private PreparedStatement saveLinkStm;
	private PreparedStatement retrieveLinkStm;
	private ObjectMapper mapper;
	
	public LocalDBLinkDAO(Connection conn) {
		this.mapper = new ObjectMapper();
		try {
			this.checkAlreadySavedStm = conn.prepareStatement("SELECT id_link FROM fom_link WHERE uri=?");
			this.saveLinkStm = conn.prepareStatement("INSERT INTO fom_link(uri, text, lang, meta) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
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
			saveLinkStm.setString(3, link.getLanguage().toString());
			saveLinkStm.setString(4, mapper.writeValueAsString(link.getMeta()));
			
			saveLinkStm.executeUpdate();
			ResultSet key = saveLinkStm.getGeneratedKeys();
			if(key.next()){
				link.setId(key.getLong(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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
				String metaString = res.getString("meta");
				Map<String, String> meta = null;
				if(metaString!="" && metaString!=null){
					try{
						meta = mapper.readValue(res.getString("meta"), new TypeReference<Map<String,String>>() { });											
					} catch (JsonParseException e) {
						e.printStackTrace();
					} catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(meta==null){
					meta = new HashMap<String, String>();
				}
				Link link = new Link(res.getString("uri"), res.getString("text"), Enum.valueOf(Language.class, res.getString("lang")), meta);
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
