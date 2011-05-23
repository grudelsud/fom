package fom.model.dao.interfaces;

import fom.model.Feed;

public interface FeedDAO {
	public long create(Feed feed);
	public Feed retrieve(long feedId);
}
