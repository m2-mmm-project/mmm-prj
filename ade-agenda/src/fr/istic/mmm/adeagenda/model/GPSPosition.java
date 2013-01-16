package fr.istic.mmm.adeagenda.model;

public class GPSPosition {

	private long lat;
	private long lng;
	
	public GPSPosition(long lat, long lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public long getLat() {
		return lat;
	}

	public void setLat(long lat) {
		this.lat = lat;
	}

	public long getLng() {
		return lng;
	}

	public void setLng(long lng) {
		this.lng = lng;
	}
	
	public void setPosition(long lat, long lng) {
		this.lat = lat;
		this.lng = lng;
	}
}
