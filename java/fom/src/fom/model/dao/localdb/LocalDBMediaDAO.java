package fom.model.dao.localdb;

import java.sql.Connection;

import fom.model.Media;
import fom.model.dao.MediaDAO;

public class LocalDBMediaDAO implements MediaDAO {

	Connection conn;
	
	public LocalDBMediaDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public long create(Media media) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Media retrieve(long mediaId) {
		// TODO Auto-generated method stub
		return null;
	}


}
