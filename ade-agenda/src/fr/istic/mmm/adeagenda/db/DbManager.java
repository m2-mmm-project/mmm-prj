package fr.istic.mmm.adeagenda.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import fr.istic.mmm.adeagenda.R;

public class DbManager extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	private static final String NAME = "ADEAgenda.sqlite";

	// RESSOURCES TABLE
	public static final String TABLE_RESOURCE = "ade_resource";

	public static final String COL_RES_ID = "id";
	public static final String COL_RES_NAME = "name";
	public static final String COL_RES_START = "start";
	public static final String COL_RES_END = "end";
	public static final String COL_RES_PLACE = "place";
	public static final String COL_RES_DESC = "description";

	public static final int IDX_COL_RES_ID = 0;
	public static final int IDX_COL_RES_NAME = 1;
	public static final int IDX_COL_RES_START = 2;
	public static final int IDX_COL_RES_END = 3;
	public static final int IDX_COL_RES_PLACE = 4;
	public static final int IDX_COL_RES_DEC = 5;

	public static final String[] FIELDS_RESOURCE = { COL_RES_ID, COL_RES_NAME,
			COL_RES_START, COL_RES_END, COL_RES_PLACE, COL_RES_DESC };

	private static final String CREATE_RES_DB = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_RESOURCE + " (" + COL_RES_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_RES_NAME
			+ " TEXT NOT NULL, " + COL_RES_START + " DATE NOT NULL, "
			+ COL_RES_END + " DATE NOT NULL, " + COL_RES_PLACE
			+ " TEXT NOT NULL, " + COL_RES_DESC + " TEXT);";

	// GPS POSITION TABLE
	public static final String TABLE_PLACE_POSITION = "place_position";

	public static final String COL_POS_ID = "id";
	public static final String COL_POS_PLACE = "place";
	public static final String COL_POS_LAT = "lat";
	public static final String COL_POS_LNG = "lng";

	public static final int IDX_COL_POS_ID = 0;
	public static final int IDX_COL_POS_NAME = 1;
	public static final int IDX_COL_POS_LAT = 2;
	public static final int IDX_COL_POS_LNG = 3;

	public static final String[] FIELDS_GPSPOSITION = { COL_POS_ID,
			COL_POS_PLACE, COL_POS_LAT, COL_POS_LNG };

	private static final String CREATE_POS_DB = "CREATE TABLE "
			+ TABLE_PLACE_POSITION + " (" + COL_POS_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_POS_PLACE
			+ " TEXT NOT NULL, " + COL_POS_LAT + " LONG NOT NULL, "
			+ COL_POS_LNG + " LONG NOT NULL);";

	private Context context;

	public DbManager(Context context) {
		super(context, NAME, null, VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("DbManager", "Create tables");

		// Create RESOURCES table
		db.execSQL(CREATE_RES_DB);

		// Create PLACE_POSITION table
		db.execSQL(CREATE_POS_DB);

		Log.v("DbManager", "Populate database with Positions");
		String[] values = context.getResources().getStringArray(R.array.sql_place_position_data);
		for (String v : values) {
			db.execSQL("INSERT INTO " + TABLE_PLACE_POSITION + " ("
					+ COL_POS_PLACE + ", " + COL_POS_LAT + ", " + COL_POS_LNG
					+ ") VALUES (" + v + ");");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_RESOURCE + ";");
		db.execSQL("DROP TABLE " + TABLE_PLACE_POSITION + ";");
		onCreate(db);
	}

}
