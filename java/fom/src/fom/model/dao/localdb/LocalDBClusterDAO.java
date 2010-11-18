package fom.model.dao.localdb;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.joda.time.DateTime;

import fom.model.Cluster;
import fom.model.GeoCluster;
import fom.model.Post;
import fom.model.Query;
import fom.model.SemanticCluster;
import fom.model.Term;
import fom.model.TimeCluster;
import fom.model.dao.interfaces.ClusterDAO;
import fom.model.dao.interfaces.DAOFactory;

public class LocalDBClusterDAO implements ClusterDAO {
	
	Connection conn;

	public LocalDBClusterDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public long create(Cluster cluster) {
		long clusterId = 0;
		try {
			PreparedStatement stm = conn.prepareStatement("INSERT INTO fom_cluster(meta,terms_meta,posts_meta,id_query) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			
			StringWriter strWriter = new StringWriter();
			ObjectMapper objMapper = new ObjectMapper();
			objMapper.writeValue(strWriter, cluster.getMeta());
			stm.setString(1, strWriter.toString());
			
			stm.setString(2, "");
			stm.setString(3, "");
			stm.setLong(4, cluster.getOriginatingQuery().getId());
			
			stm.executeUpdate();
			ResultSet generatedKeys = stm.getGeneratedKeys();
			if(generatedKeys.next()){
				clusterId = generatedKeys.getLong(1);
				for(Term term : cluster.getTerms()){
					PreparedStatement saveTermStm = conn.prepareStatement("INSERT INTO fom_clusterterm(id_term,id_cluster) VALUES(?,?)");
					saveTermStm.setLong(1, DAOFactory.getFactory().getTermDAO().create(term));
					saveTermStm.setLong(2, clusterId);
					saveTermStm.execute();
				}
				for(Post post : cluster.getPosts()){
					long postId = DAOFactory.getFactory().getPostDAO().create(post);
					PreparedStatement saveClusterPost = conn.prepareStatement("INSERT INTO fom_clusterpost(id_cluster,id_post) VALUES(?,?)");
					saveClusterPost.setLong(1, clusterId);
					saveClusterPost.setLong(2, postId);
					saveClusterPost.execute();
				}
			} else {
				System.err.println("Error creating cluster");
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
		return clusterId;
	}

	@Override
	public Cluster retrieve(long clusterId) {
		Cluster cluster = null;
		try {
			PreparedStatement stm = conn.prepareStatement("SELECT meta,id_query FROM fom_cluster WHERE id_cluster=?");
			stm.setLong(1, clusterId);
			ResultSet res = stm.executeQuery();
			if(res.next()){
				Map<String, String> meta = new ObjectMapper().readValue(res.getString("meta"), new TypeReference<Map<String,String>>() { });
		//		Map<String, String> terms_meta = new ObjectMapper().readValue(res.getString("terms_meta"), new TypeReference<Map<String,String>>() { });
		//		Map<String, String> posts_meta = new ObjectMapper().readValue(res.getString("posts_meta"), new TypeReference<Map<String,String>>() { });
				Query originatingQuery = DAOFactory.getFactory().getQueryDAO().retrieve(res.getLong("id_query"));
				if(meta.get("type").equalsIgnoreCase("geo")){
					double meanLat = new Double(meta.get("meanLat"));
					double meanLon = new Double(meta.get("meanLon"));
					double varLat = new Double(meta.get("varLat"));
					double varLon = new Double(meta.get("varLon"));
					cluster = new GeoCluster(originatingQuery, meanLat, meanLon, varLat, varLon);
				} else if(meta.get("type").equalsIgnoreCase("semantic")){
					cluster = new SemanticCluster(originatingQuery);
				} else if(meta.get("type").equalsIgnoreCase("time")){
					DateTime startTime = new DateTime(meta.get("startTime"));
					DateTime endTime = new DateTime(meta.get("endTime"));
					cluster = new TimeCluster(originatingQuery, startTime, endTime);
				}
				PreparedStatement statementPosts = conn.prepareStatement("SELECT id_post FROM fom_clusterpost WHERE id_cluster=?");
				statementPosts.setLong(1, clusterId);
				ResultSet resPosts = statementPosts.executeQuery();
				while(resPosts.next()){
					Post post = DAOFactory.getFactory().getPostDAO().retrieve(resPosts.getLong("id_post"));
					cluster.addPost(post);
				}
				PreparedStatement statementTerms = conn.prepareStatement("SELECT id_post FROM fom_clusterterm WHERE id_cluster=?");
				statementTerms.setLong(1, clusterId);
				ResultSet resTerms = statementTerms.executeQuery();
				while(resTerms.next()){
					Term term = DAOFactory.getFactory().getTermDAO().retrieve(resTerms.getLong("id_term"));
					cluster.addTerm(term);
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
		return cluster;
	}	


}
