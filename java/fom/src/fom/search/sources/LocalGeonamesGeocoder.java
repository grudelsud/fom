package fom.search.sources;

import fom.geocoding.Geocoder;

public class LocalGeonamesGeocoder implements Geocoder {

	@Override
	public double[] geocode(String location) {
		return new double[] {0,0};
	}

}
