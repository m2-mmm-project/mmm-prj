package fr.istic.mmm.adeagenda.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.istic.mmm.adeagenda.model.Event;

public class AgendaDb {

	private SQLiteDatabase db;
	private DbManager manager;
	
	public AgendaDb(Context context) {
		this.manager = new DbManager(context);
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
		int id = c.getInt(DbManager.IDX_COL_RES_ID);
		String name = c.getString(DbManager.IDX_COL_RES_NAME);
		String place = c.getString(DbManager.IDX_COL_RES_PLACE);
		
		// TODO finir le taff
		
		// closing db
		c.close();
 
		return null;
	}
}
