package fom.model.dao.localdb;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.DateTime;

import fom.model.Media;
import fom.model.Place;
import fom.model.Post;
import fom.model.TeamlifePost;
import fom.model.Term;
import fom.model.TwitterPost;
import fom.model.dao.DAOFactory;
import fom.model.dao.PostDAO;

public class LocalDBPostDAO implements PostDAO {

	Connection conn;
	
	public LocalDBPostDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public long create(Post post) {
		try {
			PreparedStatement stm = conn.prepareStatement("INSERT INTO fom_post(lat,lon,content,created,modified,timezone,meta,src,id_place) VALUES(?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			if(post.getLat()!=0 && post.getLon()!=0){
				stm.setDouble(1, post.getLat());
				stm.setDouble(2, post.getLon());				
			} else {
				stm.setNull(1, java.sql.Types.FLOAT);
				stm.setNull(2, java.sql.Types.FLOAT);
			}
			stm.setString(3, post.getContent());
			stm.setTimestamp(4, new Timestamp(post.getCreated().toDate().getTime()));
			stm.setTimestamp(5, new Timestamp(post.getModified().toDate().getTime()));
			stm.setInt(6, post.getTimezone());
			
			StringWriter strWriter = new StringWriter();
			ObjectMapper objMapper = new ObjectMapper();
			objMapper.writeValue(strWriter, post.getMeta());
			stm.setString(7, strWriter.toString());
			
			stm.setString(8, post.getSourceName());
			if(post.getPlace()!=null){
				stm.setLong(9, DAOFactory.getFactory().getPlaceDAO().create(post.getPlace()));
			} else {
				stm.setNull(9, java.sql.Types.BIGINT);
			}
			stm.executeUpdate();
			ResultSet generatedKeys = stm.getGeneratedKeys();
			if(generatedKeys.next()){
				post.setId(generatedKeys.getLong(1));			
				for(Media media : post.getMedia()){
					PreparedStatement saveMediaStm = conn.prepareStatement("INSERT INTO fom_postmedia(id_media,id_post) VALUES (?,?)");
					saveMediaStm.setLong(1,DAOFactory.getFactory().getMediaDAO().create(media));
					saveMediaStm.setLong(2, post.getId());
					saveMediaStm.execute();
				}
				for(Term term : post.getTerms()){
					PreparedStatement saveTermStm = conn.prepareStatement("INSERT INTO fom_posttag(id_term,id_post) VALUES(?,?)");
					saveTermStm.setLong(1, DAOFactory.getFactory().getTermDAO().create(term));
					saveTermStm.setLong(2, post.getId());
					saveTermStm.execute();
				}
			} else {
				System.err.println("Error creating post");
			}
		} catch (SQLException e) {
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
		return post.getId();
	}

	@Override
	public Post retrieve(long postId) {
		Post post = null;
		try {
			PreparedStatement stm = conn.prepareStatement("SELECT * FROM fom_post WHERE fom_post.id_post=?");
			stm.setLong(1, postId);
			ResultSet res = stm.executeQuery();
			if(res.next()){
				long id = res.getLong("id_post");
				Place place = DAOFactory.getFactory().getPlaceDAO().retrieve(res.getLong("id_place"));
				double lat = res.getFloat("lat");
				double lon = res.getFloat("lon");
				String content = res.getString("content");
				DateTime created = new DateTime(res.getTimestamp("created"));
				DateTime modified = new DateTime(res.getTimestamp("modified"));
				int timezone = res.getInt("timezone");
				Map<String, String> meta = new ObjectMapper().readValue(res.getString("meta"), new TypeReference<Map<String,String>>() { });
				String source = res.getString("src");
				if(source.equalsIgnoreCase("twitter")){
					post = new TwitterPost(id, lat, lon, content, created, modified, timezone, place, new Long(meta.get("tweetId")), new Integer(meta.get("twitterUserId")));
				} else if(source.equalsIgnoreCase("teamlife")){
					post = new TeamlifePost(id, lat, lon, content, created, modified, timezone, place);
				}
				
				PreparedStatement getMediaStm = conn.prepareStatement("SELECT id_media FROM fom_postmedia WHERE id_post=?");
				getMediaStm.setLong(1, post.getId());
				ResultSet mediaResSet = getMediaStm.executeQuery();
				while(mediaResSet.next()){
					post.addMedia(DAOFactory.getFactory().getMediaDAO().retrieve(mediaResSet.getLong("id_media")));
				}
				
				PreparedStatement getTermsStm = conn.prepareStatement("SELECT id_term FROM fom_posttag WHERE id_post=?");
				getTermsStm.setLong(1, post.getId());
				ResultSet termsResSet = getTermsStm.executeQuery();
				while(termsResSet.next()){
					post.addTerm(DAOFactory.getFactory().getTermDAO().retrieve(termsResSet.getLong("id_term")));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return post;
	}

	@Override
	public void attachMedia(long postId, Media media) {
		// TODO Auto-generated method stub
		
	}


}
