package fom.geocoding;

public interface Geocoder {
	
	/**
	 * Performs the geocoding of a given location name, finding its coordinates.
	 * 
	 * @param location a String containing the location to be geocoded.
	 * @return [lat, lon] if the geocoding succeeded, [0,0] if it failed.
	 * 
	 */
	double[] geocode(String location);

}
