package fr.istic.mmm.adeagenda.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbManager extends SQLiteOpenHelper {

	private static final String TABLE_RESOURCE = "adeag_resource";
	
	public static final String COL_ID		= "id";
	public static final String COL_NAME		= "name";
	public static final String COL_START	= "start";
	public static final String COL_END		= "end";
	public static final String COL_PLACE	= "place";
	public static final String COL_LAT		= "lat";
	public static final String COL_LNG		= "lng";
	public static final String COL_DESC		= "description";
	
	public static final int IDX_COL_ID		= 0;
	public static final int IDX_COL_NAME	= 1;
	public static final int IDX_COL_START	= 2;
	public static final int IDX_COL_END		= 3;
	public static final int IDX_COL_PLACE	= 4;
	public static final int IDX_COL_LAT		= 5;
	public static final int IDX_COL_LNG		= 6;
	public static final int IDX_COL_DEC		= 7;

	private static final String CREATE_DB = "CREATE TABLE " + TABLE_RESOURCE
			+ " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME
			+ " TEXT NOT NULL, " + COL_START + " DATE NOT NULL, " + COL_END
			+ " DATE NOT NULL" + COL_PLACE + " TEXT NOT NULL" + COL_LAT
			+ " LONG NOT NULL" + COL_LNG + " LONG NOT NULL" + COL_DESC
			+ " TEXT);";

	public DbManager(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DB);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_RESOURCE + ";");
		onCreate(db);
	}

}
