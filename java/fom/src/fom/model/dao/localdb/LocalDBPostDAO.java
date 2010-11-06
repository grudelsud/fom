package fom.model.dao.localdb;

import java.util.Date;
import java.util.List;

import fom.model.Post;
import fom.model.dao.PostDAO;

public class LocalDBPostDAO implements PostDAO {

	@Override
	public long create(float lat, float lon, Date dateTime, int timezone,
			String content, String source, long tw_statusid, long tw_replyid) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Post> read(float lat, float lon, String granularity,
			Date t_start, Date t_end, String t_granularity) {
		// TODO Auto-generated method stub
		return null;
	}

}
