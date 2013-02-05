package fr.istic.mmm.adeagenda.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

	public static final String[] FIELDS_GPSPOSITION = { COL_RES_ID,
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
		try {
			db.query(TABLE_PLACE_POSITION, null, null, null, null, null, null);
			Log.v("DbManager", "Database already exist");
		} catch (SQLiteException e) {
			Log.v("DbManager", "Create tables");
			// Create RESOURCES table
			db.execSQL(CREATE_RES_DB);

			// Create PLACE_POSITION table
			db.execSQL(CREATE_POS_DB);

			String androidDBPath = context.getAssets() + "/data/"
					+ context.getPackageName() + "/databases/";
			String fileDBPath = context.getPackageResourcePath();

			File androidDB = new File(androidDBPath, NAME);
			File fileDB = new File(fileDBPath, NAME);
				
			if (fileDB.exists()) {
				try {
					FileChannel src = new FileInputStream(fileDB).getChannel();

					FileChannel dst = new FileOutputStream(androidDB)
							.getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else
				Log.v("DbManager", fileDB.getAbsolutePath());
			Log.v("DbManager", "Copying the database ");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_RESOURCE + ";");
		db.execSQL("DROP TABLE " + TABLE_PLACE_POSITION + ";");
		onCreate(db);
	}

}
