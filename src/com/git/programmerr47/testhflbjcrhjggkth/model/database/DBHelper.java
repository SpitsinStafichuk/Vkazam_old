package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	public static final String HISTORY_TAG = "SongHistory";
	//TODO study how to change db version in out file (in sqlite browser)
	//TODO study to know db version from out file
	
	public DBHelper(Context context) {
		super(context, DBConstants.DATABASE, null, 5);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBConstants.SQL_CREATE_MUSIC_HISTORY_TABLE);
		db.execSQL(DBConstants.SQL_CREATE_FINGERPRINTS_TABLE);
		//for test animation
		/*for (int i = 0; i < 100; i++) {
			ContentValues values = new ContentValues();
			values.put(DBConstants.MUSIC_HISTORY_ARTIST, "Test " + i);
			values.put(DBConstants.MUSIC_HISTORY_TITLE, "Test " + i);
			long result = db.insert(DBConstants.MUSIC_HISTORY_TABLE, null, values);
			Log.v(HISTORY_TAG, i + ":" + " result = " + result);
		}*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DBConstants.SQL_DROP_FINGERPRINTS_TABLE);
		db.execSQL(DBConstants.SQL_DROP_MUSIC_HISTORY_TABLE);
		onCreate(db);
	}

}
