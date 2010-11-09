package fom.model.dao;

import java.util.List;

import fom.model.Media;
import fom.model.Post;

public interface MediaDAO {
	
	public void create(Media media);
	public List<Media> read(Post post);
	public void update(Media media, Post post);
}
