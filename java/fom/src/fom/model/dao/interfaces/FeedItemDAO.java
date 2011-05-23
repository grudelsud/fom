package fom.model.dao.interfaces;

import fom.model.FeedItem;

public interface FeedItemDAO {
	public long create(FeedItem feedItem);
	public FeedItem retrieve(long id);
}
