package fom.model.dao.localdb;

import java.sql.Connection;

import fom.model.Place;
import fom.model.dao.PlaceDAO;

public class LocalDBPlaceDAO implements PlaceDAO {

	Connection conn;
	
	public LocalDBPlaceDAO(Connection conn) {
		this.conn = conn;
	}

	@Override
	public long create(Place place) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Place retrieve(long placeId) {
		// TODO Auto-generated method stub
		return null;
	}

}
