package fr.istic.mmm.adeagenda.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.istic.mmm.adeagenda.model.Event;
import fr.istic.mmm.adeagenda.utils.DateFormater;

public class AgendaDb {

	private SQLiteDatabase db;
	private DbManager manager;

	public AgendaDb(Context context) {
		this.manager = new DbManager(context);
	}

	private void open() {
		this.db = this.manager.getWritableDatabase();
	}

	public void close() {
		if (this.db.isOpen())
			this.manager.close();
	}

	/**
	 * Clear all data
	 */
	public void clear() {
		open();
		db.execSQL("DELETE FROM " + DbManager.TABLE_RESOURCE + ";");
		close();
	}

	/**
	 * Get event by day
	 * 
	 * @param day
	 *            Day date
	 * @return List<Event> object
	 */
	public List<Event> getEventByDay(Date day) {

		open();

		Cursor cursor = this.db.query(DbManager.TABLE_RESOURCE,
				DbManager.FIELDS_RESOURCE, "date(" + DbManager.COL_RES_START
						+ ")=date('" + DateFormater.getDateSQLString(day)
						+ "')", null, null, null, null, null);

		List<Event> events = cursorToEvents(cursor);

		close();

		return events;
	}

	/**
	 * Get event by day
	 * 
	 * @param day
	 *            Day date
	 * @return List<Event> object
	 */
	public Event getNextEvent(Date day) {

		open();

		Cursor cursor = this.db.query(DbManager.TABLE_RESOURCE,
				DbManager.FIELDS_RESOURCE, "date(" + DbManager.COL_RES_START
						+ ")>date('" + DateFormater.getDateSQLString(day)
						+ "')", null, null, null, DbManager.COL_RES_START, "1");

		List<Event> events = cursorToEvents(cursor);

		close();
		if(events.size()>0){
			return events.get(0);
		}
		return null;
	}
	
	
	/**
	 * Add a event
	 * 
	 * @param event
	 *            Event
	 */
	public void add(Event event) {

		ContentValues values = new ContentValues();
		values.put(DbManager.COL_RES_NAME, event.getName());
		values.put(DbManager.COL_RES_START,
				DateFormater.getDateTimeSQLString(event.getStart()));
		values.put(DbManager.COL_RES_END,
				DateFormater.getDateTimeSQLString(event.getEnd()));
		values.put(DbManager.COL_RES_PLACE, event.getPlace());
		values.put(DbManager.COL_RES_DESC, event.getDescription());

		// Inserting Row
		open();
		db.insert(DbManager.TABLE_RESOURCE, null, values);
		close();
	}

	/**
	 * Convert a Cursor to a Event object
	 * 
	 * @param c
	 *            cursor
	 * @return Event object
	 */
	private Event cursorToEvent(Cursor c) {
		Event event = null;

		// if cursor contains data
		if (!c.isNull(DbManager.IDX_COL_RES_NAME)) {
			// getting data from db
			event = new Event(c.getString(DbManager.IDX_COL_RES_NAME),
					DateFormater.getDateTimeSQL(c
							.getString(DbManager.IDX_COL_RES_START)),
					DateFormater.getDateTimeSQL(c
							.getString(DbManager.IDX_COL_RES_END)),
					c.getString(DbManager.IDX_COL_RES_PLACE),
					c.getString(DbManager.IDX_COL_RES_DEC));
		}

		return event;
	}

	/**
	 * Convert a Cursor to a List of Event object
	 * 
	 * @param c
	 *            cursor
	 * @return List<Event> object
	 */
	private List<Event> cursorToEvents(Cursor c) {
		List<Event> events = new ArrayList<Event>();

		// move to first element
		if (c.moveToFirst()) {
			do {
				// getting data from db
				events.add(cursorToEvent(c));
			} while (c.moveToNext());
		}

		// closing cursor
		c.close();

		return events;
	}
}
