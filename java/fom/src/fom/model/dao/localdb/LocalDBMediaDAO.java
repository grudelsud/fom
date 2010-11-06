package fom.model.dao.localdb;

import java.util.List;

import fom.model.Media;
import fom.model.dao.MediaDAO;

public class LocalDBMediaDAO implements MediaDAO {

	@Override
	public long create(long id_post, String filename, String filetype,
			String description) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Media> read(long id_post) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(long id_media, long id_post, String filename,
			String filetype, String description) {
		// TODO Auto-generated method stub
		
	}

}
