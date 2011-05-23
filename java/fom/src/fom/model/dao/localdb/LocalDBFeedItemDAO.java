package fom.model.dao.localdb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.joda.time.DateTime;

import fom.model.Feed;
import fom.model.FeedItem;
import fom.model.Term;
import fom.model.dao.interfaces.DAOFactory;
import fom.model.dao.interfaces.FeedItemDAO;

public class LocalDBFeedItemDAO implements FeedItemDAO {

	private PreparedStatement checkAlreadySavedStm;
	private PreparedStatement saveFeedItemStm;
	private PreparedStatement associateFeedItemTermStm;
	private PreparedStatement getFeedItemStm;
	private PreparedStatement getFeedItemTermsStm;
	
	public LocalDBFeedItemDAO(Connection conn) {
		try {
			checkAlreadySavedStm = conn.prepareStatement("SELECT id_feeditem FROM fom_feeditem WHERE uri LIKE ?");
			saveFeedItemStm = conn.prepareStatement("INSERT INTO fom_feeditem(uri, title, description, pubDate, id_feed) VALUES(?,?,?,?,?)" ,Statement.RETURN_GENERATED_KEYS);
			associateFeedItemTermStm = conn.prepareStatement("INSERT INTO fom_feeditemterm(id_feeditem, id_term) VALUES(?,?)");
			getFeedItemStm = conn.prepareStatement("SELECT * FROM fom_feeditem WHERE id_feeditem = ?");
			getFeedItemTermsStm = conn.prepareStatement("SELECT id_term FROM fom_feeditemterm WHERE id_feeditem = ?");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public long create(FeedItem feedItem) {
		if(feedItem.getId()!=0){
			return feedItem.getId();
		}
		try {
			checkAlreadySavedStm.setString(1, feedItem.getUri());
			ResultSet res = checkAlreadySavedStm.executeQuery();
			if(res.next()){
				feedItem.setId(res.getLong("id_feeditem"));
				return feedItem.getId();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			saveFeedItemStm.setString(1, feedItem.getUri());
			saveFeedItemStm.setString(2, feedItem.getTitle());
			saveFeedItemStm.setString(3, feedItem.getDescription());
			saveFeedItemStm.setDate(4, new Date(feedItem.getPublishingDate().getMillis()));
			saveFeedItemStm.setLong(5, DAOFactory.getFactory().getFeedDAO().create(feedItem.getFeed()));
			saveFeedItemStm.executeUpdate();
			ResultSet key = saveFeedItemStm.getGeneratedKeys();
			if(key.next()){
				feedItem.setId(key.getLong(1));
				saveTerms(feedItem);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return feedItem.getId();
	}
	
	private void saveTerms(FeedItem feedItem) {
		try {
			associateFeedItemTermStm.clearBatch();
			for(Term term : feedItem.getCategories()){
				associateFeedItemTermStm.setLong(1, feedItem.getId());
				associateFeedItemTermStm.setLong(2, DAOFactory.getFactory().getTermDAO().create(term));
				associateFeedItemTermStm.addBatch();
			}
			associateFeedItemTermStm.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public FeedItem retrieve(long id) {
		FeedItem feedItem = null;
		try {
			getFeedItemStm.setLong(1, id);
			ResultSet res = getFeedItemStm.executeQuery();
			if(res.next()){
				String uri = res.getString("uri");
				String title = res.getString("title");
				String description = res.getString("description");
				DateTime pubDate = new DateTime(res.getDate("pubDate").getTime());
				Feed feed = DAOFactory.getFactory().getFeedDAO().retrieve(res.getLong("id_feed"));
				feedItem = new FeedItem(feed, title, description, uri, pubDate);
				feedItem.setId(id);
				getFeedItemTermsStm.setLong(1, id);
				ResultSet terms = getFeedItemTermsStm.executeQuery();
				while(terms.next()){
					feedItem.addCategory(DAOFactory.getFactory().getTermDAO().retrieve(terms.getLong("id_term")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return feedItem;
	}

}
