package fom.model.dao;

import java.util.Date;
import java.util.List;

import fom.model.Post;

public interface PostDAO {
	public long create(float lat, float lon, Date dateTime, int timezone, String content, String source, long tw_statusid, long tw_replyid);
	public List<Post> read(float lat, float lon, String granularity, Date t_start, Date t_end, String t_granularity);
}
