package fom.model.dao.localdb;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.DateTime;

import fom.indexing.IndexingQueue;
import fom.model.Link;
import fom.model.Media;
import fom.model.Place;
import fom.model.Post;
import fom.model.TeamlifePost;
import fom.model.Term;
import fom.model.TwitterPost;
import fom.model.dao.interfaces.DAOFactory;
import fom.model.dao.interfaces.PostDAO;
import fom.utils.StringOperations;

public class LocalDBPostDAO implements PostDAO {

	private PreparedStatement checkAlreadySavedStm;
	private PreparedStatement savePostStm;
	private PreparedStatement saveMediaStm;
	private	PreparedStatement saveTermStm;
	private PreparedStatement saveLinkStm;
	private PreparedStatement getPostStm;
	private PreparedStatement getMediaStm;
	private PreparedStatement getTermsStm;
	private PreparedStatement getLinksStm;
	private ObjectMapper objMapper;
	private Connection conn;

	
	public LocalDBPostDAO(Connection conn) {
		try {
			checkAlreadySavedStm = conn.prepareStatement("SELECT id_post FROM fom_post WHERE src = ? AND src_id = ?");
			savePostStm = conn.prepareStatement("INSERT INTO fom_post(lat,lon,content,created,modified,timezone,meta,src,id_place,src_id) VALUES(?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			saveMediaStm = conn.prepareStatement("INSERT INTO fom_postmedia(id_media,id_post) VALUES (?,?)");
			saveTermStm = conn.prepareStatement("INSERT INTO fom_posttag(id_term,id_post) VALUES(?,?)");
			saveLinkStm = conn.prepareStatement("INSERT INTO fom_postlink(id_post, id_link) VALUES(?,?)");
			getPostStm  = conn.prepareStatement("SELECT * FROM fom_post WHERE fom_post.id_post=?");
			getMediaStm = conn.prepareStatement("SELECT id_media FROM fom_postmedia WHERE id_post=?");
			getTermsStm = conn.prepareStatement("SELECT id_term FROM fom_posttag WHERE id_post=?");
			getLinksStm = conn.prepareStatement("SELECT id_link FROM fom_postlink WHERE id_post=?");
			this.conn = conn;
			objMapper = new ObjectMapper();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public long create(Post post) {
		try {
			if(post.getId()!=0){
				return post.getId();
			}
			checkAlreadySavedStm.setString(1, post.getSourceName());
			checkAlreadySavedStm.setLong(2, post.getSourceId());
			ResultSet checkAlreadySavedRes = checkAlreadySavedStm.executeQuery();
			if(checkAlreadySavedRes.next()){
				post.setId(checkAlreadySavedRes.getLong("id_post"));
				return post.getId();
			}
			
			if(post.getLat()!=0 || post.getLon()!=0){
				savePostStm.setDouble(1, post.getLat());
				savePostStm.setDouble(2, post.getLon());				
			} else {
				savePostStm.setNull(1, java.sql.Types.FLOAT);
				savePostStm.setNull(2, java.sql.Types.FLOAT);
			}
			savePostStm.setString(3, post.getContent());
			savePostStm.setTimestamp(4, new Timestamp(post.getCreated().toDate().getTime()));
			savePostStm.setTimestamp(5, new Timestamp(post.getModified().toDate().getTime()));
			savePostStm.setInt(6, post.getTimezone());
			
			StringWriter strWriter = new StringWriter();
			objMapper.writeValue(strWriter, post.getMeta());
			savePostStm.setString(7, strWriter.toString());
			
			savePostStm.setString(8, post.getSourceName());
			if(post.getPlace()!=null){
				savePostStm.setLong(9, DAOFactory.getFactory().getPlaceDAO().create(post.getPlace()));
			} else {
				savePostStm.setNull(9, java.sql.Types.BIGINT);
			}
			
			savePostStm.setLong(10, post.getSourceId());
			
			savePostStm.executeUpdate();
			ResultSet generatedKeys = savePostStm.getGeneratedKeys();
			if(generatedKeys.next()){
				post.setId(generatedKeys.getLong(1));			
				for(Media media : post.getMedia()){
					saveMediaStm.setLong(1,DAOFactory.getFactory().getMediaDAO().create(media));
					saveMediaStm.setLong(2, post.getId());
					saveMediaStm.execute();
				}
				for(Term term : post.getTerms()){
					saveTermStm.setLong(1, DAOFactory.getFactory().getTermDAO().create(term));
					saveTermStm.setLong(2, post.getId());
					saveTermStm.execute();
				}
				for(Link link : post.getLinks()){
					saveLinkStm.setLong(1, post.getId());
					saveLinkStm.setLong(2, DAOFactory.getFactory().getLinkDAO().create(link));
					saveLinkStm.execute();
				}
				IndexingQueue.getInstance().addToQueue(post);
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
			getPostStm.setLong(1, postId);
			ResultSet res = getPostStm.executeQuery();
			if(res.next()){
				Place place = DAOFactory.getFactory().getPlaceDAO().retrieve(res.getLong("id_place"));
				double lat = res.getFloat("lat");
				double lon = res.getFloat("lon");
				String content = res.getString("content");
				DateTime created = new DateTime(res.getTimestamp("created"));
				DateTime modified = new DateTime(res.getTimestamp("modified"));
				int timezone = res.getInt("timezone");
				Map<String, String> meta = objMapper.readValue(res.getString("meta"), new TypeReference<Map<String,String>>() { });
				String source = res.getString("src");
				if(source.equalsIgnoreCase("twitter")){
					post = new TwitterPost(postId, lat, lon, content, created, modified, timezone, place, new Long(meta.get("tweetId")), new Integer(meta.get("twitterUserId")));
				} else if(source.equalsIgnoreCase("teamlife")){
					post = new TeamlifePost(postId, lat, lon, content, created, modified, timezone, place);
				}
				
				getMediaStm.setLong(1, post.getId());
				ResultSet mediaResSet = getMediaStm.executeQuery();
				while(mediaResSet.next()){
					post.addMedia(DAOFactory.getFactory().getMediaDAO().retrieve(mediaResSet.getLong("id_media")));
				}
				
				getTermsStm.setLong(1, post.getId());
				ResultSet termsResSet = getTermsStm.executeQuery();
				while(termsResSet.next()){
					post.addTerm(DAOFactory.getFactory().getTermDAO().retrieve(termsResSet.getLong("id_term")));
				}
				
				getLinksStm.setLong(1, post.getId());
				ResultSet linksResSet = getLinksStm.executeQuery();
				while(linksResSet.next()){
					post.addLink(DAOFactory.getFactory().getLinkDAO().retrieve(linksResSet.getLong("id_link")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return post;
	}
	
	public List<Post> retrieve(List<String> terms, DateTime fromDate, DateTime toDate, double lat, double lon, int radius){
		List<Post> results = new ArrayList<Post>();
		
		String query = new String("SELECT DISTINCT id_post " +
									"FROM fom_post " +
									"WHERE ");
		for(int termCount=0; termCount<terms.size(); termCount++){
			if(termCount==0){
				query = query.concat("(");			
			}
			query = query.concat("content LIKE CONCAT('% ',?,' %') " +
								"OR content LIKE CONCAT(?,' %') " +
								"OR content LIKE CONCAT('% ',?) " +
								"OR content LIKE CONCAT('% ',?,' %') " +
								"OR content LIKE CONCAT(?,' %') " +
								"OR content LIKE CONCAT('% ',?)");
			if(termCount+1!=terms.size()){
				query = query.concat(" OR ");
			} else {
				query = query.concat(") ");
			}
		}
		if(fromDate!=null){
			if(terms.size()!=0){
				query = query.concat("AND ");
			}
			query = query.concat("created >= ? ");
		}
		if(toDate!=null){
			if(terms.size()!=0 || fromDate!=null){
				query = query.concat("AND ");
			}
			query = query.concat("created <= ? ");
		}
		if(radius!=0){
			if(terms.size()!=0 || fromDate!=null || toDate!=null){
				query = query.concat("AND ");
			}
			query = query.concat("(lat > ? AND lat < ? AND lon > ? AND lon < ?)");
		}
		
		try {
			PreparedStatement stm = conn.prepareStatement(query);
			for(int termCount=0; termCount<terms.size(); termCount++){
				stm.setString(termCount*6 + 1, terms.get(termCount));		
				stm.setString(termCount*6 + 2, terms.get(termCount));			
				stm.setString(termCount*6 + 3, terms.get(termCount));		
				stm.setString(termCount*6 + 4, StringOperations.hashtagify(terms.get(termCount)));			
				stm.setString(termCount*6 + 5, StringOperations.hashtagify(terms.get(termCount)));			
				stm.setString(termCount*6 + 6, StringOperations.hashtagify(terms.get(termCount)));			
			}
			if(fromDate!=null){
				stm.setTimestamp(terms.size()*6 + 1, new Timestamp(fromDate.toDate().getTime()));
			}
			if(toDate!=null){
				int numberOfPreviousParams = 0;
				if(fromDate!=null){
					numberOfPreviousParams++;
				}
				stm.setTimestamp(terms.size()*6 + numberOfPreviousParams + 1, new Timestamp(toDate.toDate().getTime()));
			}
			if(radius!=0){
				int numberOfPreviousParams = 0;
				if(fromDate!=null){
					numberOfPreviousParams++;
				}
				if(toDate!=null){
					numberOfPreviousParams++;
				}
				//TODO: FIX THE FUCKING BUG RIGHT HERE!!!!!
				stm.setDouble(terms.size()*6 + 1 + numberOfPreviousParams, lat - 10);
				stm.setDouble(terms.size()*6 + 2 + numberOfPreviousParams, lat + 10);
				stm.setDouble(terms.size()*6 + 3 + numberOfPreviousParams, lon - 10);
				stm.setDouble(terms.size()*6 + 4 + numberOfPreviousParams, lon + 10);
			}
			ResultSet res = stm.executeQuery();
			while(res.next()){
				results.add(this.retrieve(res.getLong("id_post")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
	
	public List<Post> getAllPosts(){
		List<Post> posts = new ArrayList<Post>();
		try {
			PreparedStatement stm = conn.prepareStatement("SELECT id_post FROM fom_post");
			ResultSet res = stm.executeQuery();
			while(res.next()){
				posts.add(DAOFactory.getFactory().getPostDAO().retrieve(res.getLong("id_post")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return posts;
	}
	
}
