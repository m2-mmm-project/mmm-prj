package fr.istic.mmm.adeagenda;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.istic.mmm.adeagenda.utils.Config;

public class MapActivity extends FragmentActivity {

	private GoogleMap map;
	private MarkerOptions myMarker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_map)).getMap();

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(Config.CENTER_ISTIC,
				18));

		// Add a marker if the bundle contains a position
		if (getIntent().hasExtra(Config.MAP_POSITION_LAT)
				&& getIntent().hasExtra(Config.MAP_POSITION_LNG)
				&& getIntent().hasExtra(Config.MAP_PLACE_NAME)) {

			Log.v("MapActivity", "Add a marker");
			
			

			final double posLat = getIntent().getDoubleExtra(Config.MAP_POSITION_LAT,
					0);
			final double posLng = getIntent().getDoubleExtra(Config.MAP_POSITION_LNG,
					0);
			
			final MarkerOptions batMarker = new MarkerOptions().position(
					new LatLng(posLat, posLng)).title(getIntent().getStringExtra(Config.MAP_PLACE_NAME));
			map.addMarker(batMarker);
			map.setOnMarkerClickListener(new OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker marker) {
					if (marker.equals(batMarker)) {
						Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
								Uri.parse("http://maps.google.com/maps?saddr=" + map.getMyLocation().getLatitude() +","+ map.getMyLocation().getLongitude()+"&daddr="+ posLat+","+posLng));
								startActivity(intent);
					}
					return false;
				}
			});
		}
		
		map.setMyLocationEnabled(true);
		
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		      // Called when a new location is found by the network location provider.
		      makeUseOfNewLocation(location);
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		  };

		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
//		map.addMarker(new MarkerOptions().position(
//				mapC.getMyLocation()).title(getIntent().getStringExtra(Config.MAP_PLACE_NAME)));
	
		// Zoom in, animating the camera.
		// map.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
	}
	
	/**
	 * Updates current user position on map with GPS coordinates
	 * @param location
	 */
	private void makeUseOfNewLocation(Location location) {
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}

}