package fr.istic.mmm.adeagenda;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import fr.istic.mmm.adeagenda.R;

public class MapActivity extends Activity {

	static final LatLng PLACE_02B_E007 = new LatLng(48.116219, -1.638167);

	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		map.addMarker(new MarkerOptions()
				.position(PLACE_02B_E007)
				.title("02B - E007")
				.snippet("Salle de TP")
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.ic_launcher)));

		// Move the camera instantly to the point with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(PLACE_02B_E007, 18));

		// Zoom in, animating the camera.
//		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}