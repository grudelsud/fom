package fom.model.dao;

import fom.model.Media;

public interface MediaDAO {
	public long create(Media media);
	public Media retrieve(long mediaId);
}
