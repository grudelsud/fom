package fom.model.dao.localdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import fom.model.Place;
import fom.model.dao.interfaces.PlaceDAO;

public class LocalDBPlaceDAO implements PlaceDAO {

	private PreparedStatement stm;
	private PreparedStatement getPlaceStm;
	
	public LocalDBPlaceDAO(Connection conn) {
		try {
			stm  = conn.prepareStatement("INSERT INTO fom_place(lat, lon, description, granularity) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			getPlaceStm  = conn.prepareStatement("SELECT * FROM fom_place WHERE id_place=?");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public long create(Place place) {
		long placeId = 0;
		try {
			stm.setDouble(1, place.getLat());
			stm.setDouble(2, place.getLon());
			stm.setString(3, place.getDescription());
			stm.setString(4, place.getGranularity());
			
			stm.executeUpdate();
			ResultSet generatedKeys = stm.getGeneratedKeys();
			if(generatedKeys.next()){
				placeId=generatedKeys.getLong(1);
			} else {
				System.err.println("Error creating place");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return placeId;
	}

	@Override
	public Place retrieve(long placeId) {
		Place place = null;
		try{
			getPlaceStm.setLong(1, placeId);
			ResultSet res = getPlaceStm.executeQuery();
			while(res.next()){
				double lat = res.getDouble("lat");
				double lon = res.getDouble("lon");
				String description = res.getString("description");
				String granularity = res.getString("granularity");
				place = new Place(lat, lon, description, granularity);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return place;
	}

}
