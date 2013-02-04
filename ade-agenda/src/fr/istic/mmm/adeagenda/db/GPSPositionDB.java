package fr.istic.mmm.adeagenda.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.LatLng;

public class GPSPositionDB {

	private SQLiteDatabase db;
	private DbManager manager;

	private String[] fieldsPosition = { DbManager.COL_POS_ID,
			DbManager.COL_POS_PLACE, DbManager.COL_POS_LAT,
			DbManager.COL_POS_LNG };

	public GPSPositionDB(Context context) {
		this.manager = new DbManager(context);
	}

	public void open() {
		this.db = this.manager.getWritableDatabase();
	}

	public void close() {
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

		Cursor cursor = this.db.query(DbManager.TABLE_GPSPOSITION,
				fieldsPosition, DbManager.COL_POS_PLACE + "=?",
				new String[] { name }, null, null, null, null);

		return cursorToLatLng(cursor);
	}

	/**
	 * Convert a Cursor to a LatLng object
	 * 
	 * @param c
	 *            cursor
	 * @return LatLng position
	 */
	private LatLng cursorToLatLng(Cursor c) {
		// no element
		if (c.getCount() == 0)
			return null;

		// move to first element
		c.moveToFirst();

		// getting data from db
		LatLng position = new LatLng(c.getDouble(DbManager.IDX_COL_POS_LAT),
				c.getDouble(DbManager.IDX_COL_POS_LNG));

		// closing db
		c.close();

		return position;
	}

	private void add() {

		ContentValues values = new ContentValues();
		values.put(DbManager.COL_POS_PLACE, "oo");
		values.put(DbManager.COL_POS_LAT, "1.0");
		values.put(DbManager.COL_POS_LNG, "2.0");

		// Inserting Row
		open();
		db.insert(DbManager.TABLE_GPSPOSITION, null, values);
		db.close();
	}
}