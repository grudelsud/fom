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
	
	private PreparedStatement stm;
//	private PreparedStatement saveTermStm;
//	private PreparedStatement saveClusterPost;
	private PreparedStatement saveClusterStm;
	private PreparedStatement statementPosts;
	private PreparedStatement statementTerms;
	private ObjectMapper objMapper;
	
	public LocalDBClusterDAO(Connection conn) {
		try {
			stm = conn.prepareStatement("INSERT INTO fom_cluster(meta,terms_meta,posts_meta,id_query,id_parent,type, lat, lon) VALUES(?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
//			saveTermStm = conn.prepareStatement("INSERT INTO fom_clusterterm(id_term,id_cluster) VALUES(?,?)");
//			saveClusterPost = conn.prepareStatement("INSERT INTO fom_clusterpost(id_cluster,id_post) VALUES(?,?)");
			saveClusterStm = conn.prepareStatement("SELECT meta,id_query FROM fom_cluster WHERE id_cluster=?");
			statementPosts = conn.prepareStatement("SELECT id_post FROM fom_clusterpost WHERE id_cluster=?");
			statementTerms = conn.prepareStatement("SELECT id_post FROM fom_clusterterm WHERE id_cluster=?");
			objMapper = new ObjectMapper();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public long create(Cluster cluster) {
		if(cluster.getId()!=0){
			return cluster.getId();
		}
		long clusterId = 0;
		try {
//			stm = conn.prepareStatement("INSERT INTO fom_cluster(meta,terms_meta,posts_meta,id_query,id_parent,type) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			
			StringWriter strWriter = new StringWriter();
			objMapper.writeValue(strWriter, cluster.getMeta());
			stm.setString(1, strWriter.toString());
			
			String terms_meta = new String("");
			for(int i=0; i<cluster.getTerms().size(); i++){
				terms_meta = terms_meta.concat("\"" + cluster.getTerms().get(i).getName() + "\"");
				if(i!=cluster.getTerms().size()-1){
					terms_meta = terms_meta.concat(";");
				}
			}
			stm.setString(2, terms_meta);
			
			String posts_meta = new String("");
			for(int i=0; i<cluster.getPosts().size(); i++){
				posts_meta = posts_meta.concat(Long.toString(DAOFactory.getFactory().getPostDAO().create(cluster.getPosts().get(i))) + " ");
			}
			stm.setString(3, posts_meta.trim());

			stm.setLong(4, cluster.getOriginatingQuery().getId());
			if(cluster.getTypeId()!=1){
				stm.setLong(5, DAOFactory.getFactory().getClusterDAO().create(cluster.getParentCluster()));
			} else {
				stm.setLong(5, 0);
			}
			stm.setInt(6, cluster.getTypeId());
			if(cluster.getTypeId()==2){
				stm.setDouble(7, cluster.getMeanLat());
				stm.setDouble(8, cluster.getMeanLon());
			} else {
				stm.setDouble(7, 0);
				stm.setDouble(8, 0);
			}
			stm.executeUpdate();
			
			ResultSet generatedKeys = stm.getGeneratedKeys();
			if(generatedKeys.next()){
				clusterId = generatedKeys.getLong(1);
				cluster.setId(clusterId);
			//	saveTerms(cluster);
			//	savePosts(cluster);
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

	/*
	private void savePosts(Cluster cluster) {
		try {
			saveClusterPost.clearBatch();
			for(Post post : cluster.getPosts()){
				saveClusterPost.setLong(1, cluster.getId());
				saveClusterPost.setLong(2, DAOFactory.getFactory().getPostDAO().create(post));
				saveClusterPost.addBatch();
			}
			saveClusterPost.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveTerms(Cluster cluster) {
		try {
			saveTermStm.clearBatch();
			for(Term term : cluster.getTerms()){
				saveTermStm.setLong(2, cluster.getId());
				saveTermStm.setLong(1, DAOFactory.getFactory().getTermDAO().create(term));
				saveTermStm.addBatch();
			}
			saveTermStm.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	
	//TODO: update retrieval of cluster, the db structure is modified.
	
	@Override
	public Cluster retrieve(long clusterId) {
		System.err.println("!!! UPDATE ClusterDAO.retrieve() !!!");
		Cluster cluster = null;
		try {
			saveClusterStm.setLong(1, clusterId);
			ResultSet res = saveClusterStm.executeQuery();
			if(res.next()){
				Map<String, String> meta = objMapper.readValue(res.getString("meta"), new TypeReference<Map<String,String>>() { });
		//		Map<String, String> terms_meta = new ObjectMapper().readValue(res.getString("terms_meta"), new TypeReference<Map<String,String>>() { });
		//		Map<String, String> posts_meta = new ObjectMapper().readValue(res.getString("posts_meta"), new TypeReference<Map<String,String>>() { });
				Query originatingQuery = DAOFactory.getFactory().getQueryDAO().retrieve(res.getLong("id_query"));
				if(meta.get("type").equalsIgnoreCase("geo")){
					cluster = new GeoCluster(originatingQuery, null);
					//TODO: set the proper parent!
					System.err.println("!!! FIX THE BUG IN LocalDBClusterDAO.retrieve() !!!");
				} else if(meta.get("type").equalsIgnoreCase("semantic")){
					cluster = new SemanticCluster(originatingQuery, null, 0);
					System.err.println("!!! FIX THE BUG IN LocalDBClusterDAO.retrieve() !!!");
				} else if(meta.get("type").equalsIgnoreCase("time")){
					cluster = new TimeCluster(originatingQuery);
				}

				statementPosts.setLong(1, clusterId);
				ResultSet resPosts = statementPosts.executeQuery();
				while(resPosts.next()){
					Post post = DAOFactory.getFactory().getPostDAO().retrieve(resPosts.getLong("id_post"));
					cluster.addPost(post);
				}
			
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
