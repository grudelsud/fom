package fom.model.dao;

import fom.model.Media;
import fom.model.Post;


public interface PostDAO {
	public long create(Post post);
	public Post retrieve(long postId);
	public void attachMedia(long postId, Media media);
}
