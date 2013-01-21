package fr.istic.mmm.adeagenda.db;

import fr.istic.mmm.adeagenda.model.Event;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AgendaDb {

	private static final int VERSION = 1;
	private static final String NAME = "agenda.db";
	
	private SQLiteDatabase db;
	private DbManager manager;
	
	public AgendaDb(Context context) {
		// create the db
		this.manager = new DbManager(context, NAME, null, VERSION);
	}
	
	public void open() {
		this.db = this.manager.getWritableDatabase();
	}
	
	public void close() {
		this.manager.close();
	}
	
	/**
	 * Convert a Cursor to a Event object
	 * @param c cursor
	 * @return Event object
	 */
	private Event cursorToEvent(Cursor c){
		// no element
		if (c.getCount() == 0) return null;
			
		// move to first element
		c.moveToFirst();
		
		// getting data from db
		int id = c.getInt(DbManager.IDX_COL_ID);
		String name = c.getString(DbManager.IDX_COL_NAME);
		String place = c.getString(DbManager.IDX_COL_PLACE);
		
		// TODO finir le taff
		
		// closing db
		c.close();
 
		return null;
	}
}
