package fom.model.dao.localdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import fom.langidentification.LanguageIdentifier.Language;
import fom.model.Feed;
import fom.model.Term;
import fom.model.dao.interfaces.DAOFactory;
import fom.model.dao.interfaces.FeedDAO;

public class LocalDBFeedDAO implements FeedDAO {

	private PreparedStatement checkAlreadySavedStm;
	private PreparedStatement saveFeedStm;
	private PreparedStatement retrieveFeedStm;
	
	public LocalDBFeedDAO(Connection conn) {
		try {
			checkAlreadySavedStm = conn.prepareStatement("SELECT id_feed FROM fom_feed WHERE url = ?");
			saveFeedStm = conn.prepareStatement("INSERT INTO fom_feed(url, id_category, language, lat, lon, radius) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			retrieveFeedStm = conn.prepareStatement("SELECT * FROM fom_feed WHERE id_feed = ?");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public long create(Feed feed) {
		if(feed.getId()!=0){
			return feed.getId();
		}
		try {
			checkAlreadySavedStm.setString(1, feed.getUrl());
			ResultSet res = checkAlreadySavedStm.executeQuery();
			if(res.next()){
				feed.setId(res.getLong("id_feed"));
				return feed.getId();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			saveFeedStm.setString(1, feed.getUrl());
			saveFeedStm.setLong(2, DAOFactory.getFactory().getTermDAO().create(feed.getCategory()));
			saveFeedStm.setString(3, feed.getLanguage().toString());
			if(feed.isNational()){
				saveFeedStm.setDouble(4, 0);
				saveFeedStm.setDouble(5, 0);
				saveFeedStm.setInt(6, 0);
			} else {
				saveFeedStm.setDouble(4, feed.getLat());
				saveFeedStm.setDouble(5, feed.getLon());
				saveFeedStm.setInt(6, feed.getInfluenceRadiusKm());
			}
			saveFeedStm.executeUpdate();
			ResultSet key = saveFeedStm.getGeneratedKeys();
			if(key.next()){
				feed.setId(key.getLong(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return feed.getId();
	}

	@Override
	public Feed retrieve(long feedId) {
		try {
			retrieveFeedStm.setLong(1, feedId);
			ResultSet res = retrieveFeedStm.executeQuery();
			if(res.next()){
				long id = res.getLong("id_feed");
				String url = res.getString("url");
				Language lang = Language.valueOf(res.getString("language"));
				Term category = DAOFactory.getFactory().getTermDAO().retrieve(res.getLong("id_term"));
				double lat = res.getDouble("lat");
				double lon = res.getDouble("lon");
				int radius = res.getInt("radius");
				if(lat==0 && lon==0 && radius==0){
					Feed result = new Feed(url, category, lang);
					result.setId(id);
					return result;
				} else {
					Feed result = new Feed(url, category, lang, lat, lon, radius);
					result.setId(id);
					return result;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

}
