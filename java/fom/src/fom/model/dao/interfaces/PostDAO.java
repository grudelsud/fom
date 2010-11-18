package fom.model.dao.interfaces;

import fom.model.Post;


public interface PostDAO {
	public long create(Post post);
	public Post retrieve(long postId);
}
