package fom.model.dao.interfaces;

import java.util.List;

import org.joda.time.DateTime;

import fom.model.Post;


public interface PostDAO {
	public long create(Post post);
	public Post retrieve(long postId);
	public List<Post> retrieve(List<String> terms, DateTime fromDate, DateTime toDate, double lat, double lon, int radius, String sourceName);
//	public List<Post> getAllPosts();
}
