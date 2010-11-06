package fom.model.dao;

import java.util.Date;

public interface QueryDAO {
	public long create(float lat, float lon, Date datetime, int timezone);
}
