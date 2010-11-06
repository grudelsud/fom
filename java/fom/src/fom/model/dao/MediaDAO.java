package fom.model.dao;

import java.util.List;

import fom.model.Media;

public interface MediaDAO {
	public long create(long id_post, String filename, String filetype, String description);
	public List<Media> read(long id_post);
	public void update(long id_media, long id_post, String filename, String filetype, String description);
}
