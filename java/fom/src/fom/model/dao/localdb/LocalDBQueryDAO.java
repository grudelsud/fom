package fom.model.dao.localdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.joda.time.DateTime;

import fom.model.Cluster;
import fom.model.Query;
import fom.model.Term;
import fom.model.dao.interfaces.DAOFactory;
import fom.model.dao.interfaces.QueryDAO;

public class LocalDBQueryDAO implements QueryDAO {
	
	private PreparedStatement stm;
	private PreparedStatement saveTermStm;
	private PreparedStatement saveQueryStm;
	private PreparedStatement getClusterStm;
	private PreparedStatement getTermsStm;
	
	public LocalDBQueryDAO(Connection conn) {
		try {
			stm = conn.prepareStatement("INSERT INTO fom_query(id_user, query, t_start, t_end, t_granularity, lat, lon, geo_granularity, created, timezone) VALUES(?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			saveTermStm = conn.prepareStatement("INSERT INTO fom_querytag(id_term,id_query) VALUES(?,?)");
			saveQueryStm = conn.prepareStatement("SELECT * FROM fom_query WHERE id_query=?");
			getClusterStm = conn.prepareStatement("SELECT id_cluster FROM fom_cluster WHERE id_query=?");
			getTermsStm = conn.prepareStatement("SELECT id_term FROM fom_querytag WHERE id_query=?");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public long create(Query query) {
		long queryId=0;
		try {
			stm.setLong(1, query.getUserId());
			stm.setString(2, query.getQuery());
			stm.setTimestamp(3, new Timestamp(query.getStartTime().toDate().getTime()));
			stm.setTimestamp(4, new Timestamp(query.getEndTime().toDate().getTime()));
			stm.setString(5, query.getTimeGranularity());
			stm.setDouble(6, query.getLat());
			stm.setDouble(7, query.getLon());
			stm.setString(8, query.getGeoGranularity());
			stm.setTimestamp(9, new Timestamp(query.getCreated().toDate().getTime()));
			stm.setInt(10, query.getTimezone());
			stm.executeUpdate();
			
			ResultSet generatedKeys = stm.getGeneratedKeys();
			if(generatedKeys.next()){
				queryId=generatedKeys.getLong(1);
				query.setId(queryId);
				for(Term term : query.getTerms()){
					saveTermStm.setLong(1, DAOFactory.getFactory().getTermDAO().create(term));
					saveTermStm.setLong(2, query.getId());
					saveTermStm.execute();
				}
				for(Cluster cluster : query.getClusters()){
					DAOFactory.getFactory().getClusterDAO().create(cluster);		
				}
			} else {
				System.err.println("Error creating query");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return queryId;
	}

	@Override
	public Query retrieve(long queryId) {
		Query query = null;
		
		try {
			saveQueryStm.setLong(1, queryId);
			ResultSet res = saveQueryStm.executeQuery();
			while(res.next()){
				long userId = res.getLong("id_user");
				String originalQuery = res.getString("query");
				DateTime startTime = new DateTime(res.getTimestamp("t_start"));
				DateTime endTime = new DateTime(res.getTimestamp("t_end"));
				String timeGranularity = res.getString("t_granularity");
				double lat = res.getDouble("lat");
				double lon = res.getDouble("lon");
				String geoGranularity = res.getString("geo_granularity");
				DateTime created = new DateTime(res.getTimestamp("created"));
				int timezone = res.getInt("timezone");
				query = new Query(queryId, userId, originalQuery, startTime, endTime, timeGranularity, lat, lon, geoGranularity, created, timezone);
				
				getClusterStm.setLong(1, queryId);
				ResultSet getClusterRes = getClusterStm.executeQuery();
				while(getClusterRes.next()){
					query.addCluster(DAOFactory.getFactory().getClusterDAO().retrieve(getClusterRes.getLong("id_cluster")));
				}
				
				getTermsStm.setLong(1, queryId);
				ResultSet getTermsRes = getTermsStm.executeQuery();
				while(getTermsRes.next()){
					query.addTerm(DAOFactory.getFactory().getTermDAO().retrieve(getTermsRes.getLong("id_term")));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return query;
	}



}
