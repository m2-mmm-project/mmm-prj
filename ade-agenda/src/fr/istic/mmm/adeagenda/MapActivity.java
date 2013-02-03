package fr.istic.mmm.adeagenda;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.istic.mmm.adeagenda.utils.Config;

public class MapActivity extends FragmentActivity {

	static final LatLng CENTER_ISTIC = new LatLng(48.115671, -1.63813);

	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_map)).getMap();

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(CENTER_ISTIC, 18));

		// Add a marker if the bundle contains a position
		if (getIntent().hasExtra(Config.MAP_POSITION_LAT)
				&& getIntent().hasExtra(Config.MAP_POSITION_LNG)) {

			Log.v("MapActivity", "Add a marker");

			double posLat = getIntent().getDoubleExtra(Config.MAP_POSITION_LAT,
					0);
			double posLng = getIntent().getDoubleExtra(Config.MAP_POSITION_LNG,
					0);
			map.addMarker(new MarkerOptions()
					.position(new LatLng(posLat, posLng)).title("02B - E007")
					.snippet("Salle de TP"));
		}

		// Zoom in, animating the camera.
		// map.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_map, menu);
		return true;
	}

}