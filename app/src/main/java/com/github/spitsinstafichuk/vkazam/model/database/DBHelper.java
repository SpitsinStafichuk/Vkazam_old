package com.github.spitsinstafichuk.vkazam.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	public static final String HISTORY_TAG = "SongHistory";
	
	public DBHelper(Context context) {
		super(context, DBConstants.DATABASE, null, 11);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBConstants.SQL_CREATE_MUSIC_HISTORY_TABLE);
		db.execSQL(DBConstants.SQL_CREATE_FINGERPRINTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DBConstants.SQL_DROP_FINGERPRINTS_TABLE);
		db.execSQL(DBConstants.SQL_DROP_MUSIC_HISTORY_TABLE);
		onCreate(db);
	}

}
