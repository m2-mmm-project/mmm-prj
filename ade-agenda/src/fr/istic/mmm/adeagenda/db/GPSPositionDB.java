package fr.istic.mmm.adeagenda.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

import fr.istic.mmm.adeagenda.utils.Config;

public class GPSPositionDB {

	private SQLiteDatabase db;
	private DbManager manager;

	public GPSPositionDB(Context context) {
		this.manager = new DbManager(context);
	}

	public void open() {
		this.db = this.manager.getWritableDatabase();
	}

	public void close() {
		if (this.db.isOpen())
			this.manager.close();
	}

	/**
	 * Get place position by name
	 * 
	 * @param name
	 *            Place name
	 * @return LatLng position
	 */
	public LatLng getPositionByName(String name) {

		open();

		Cursor cursor = this.db.query(DbManager.TABLE_PLACE_POSITION,
				DbManager.FIELDS_GPSPOSITION, DbManager.COL_POS_PLACE + " = ?",
				new String[] { name }, null, null, null, null);

		LatLng position = cursorToLatLng(cursor);
		
		close();
		
		return position;
	}

	/**
	 * Add a place position
	 * 
	 * @param name
	 *            Name of the place
	 * @param lat
	 *            Latitude
	 * @param lng
	 *            Longitude
	 */
	public void add(String name, double lat, double lng) {

		ContentValues values = new ContentValues();
		values.put(DbManager.COL_POS_PLACE, name);
		values.put(DbManager.COL_POS_LAT, lat);
		values.put(DbManager.COL_POS_LNG, lng);

		// Inserting Row
		open();
		db.insert(DbManager.TABLE_PLACE_POSITION, null, values);
		close();
	}

	/**
	 * Convert a Cursor to a LatLng object
	 * 
	 * @param c
	 *            cursor
	 * @return LatLng position
	 */
	private LatLng cursorToLatLng(Cursor c) {
		LatLng position = Config.CENTER_ISTIC;

		// move to first element
		if (c.moveToFirst()) {
			// getting data from db
			position = new LatLng(c.getDouble(DbManager.IDX_COL_POS_LAT),
					c.getDouble(DbManager.IDX_COL_POS_LNG));
		}

		// closing cursor
		c.close();

		return position;
	}
}