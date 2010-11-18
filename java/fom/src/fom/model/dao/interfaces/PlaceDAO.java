package fom.model.dao.interfaces;

import fom.model.Place;

public interface PlaceDAO {
	public long create(Place place);
	public Place retrieve(long placeId);
}
