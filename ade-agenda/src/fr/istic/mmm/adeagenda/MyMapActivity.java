package fr.istic.mmm.adeagenda;

import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class MyMapActivity extends MapActivity {

	static final GeoPoint PLACE_02B_E007 = new GeoPoint(48116219, -1638167);
	private MapView map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		map = (MapView) findViewById(R.id.map);
		map.getController().animateTo(PLACE_02B_E007);
		map.getController().setZoom(18);
		
		// TODO : Affichage d'un marker
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected boolean isLocationDisplayed() {
		return true;
	}

}