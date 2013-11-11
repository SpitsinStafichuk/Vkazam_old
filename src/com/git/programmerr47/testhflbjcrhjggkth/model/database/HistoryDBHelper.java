package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistoryDBHelper extends SQLiteOpenHelper {
	//TODO study how to change db version in out file (in sqlite browser)
	//TODO study to know db version from out file
	
	public HistoryDBHelper(Context context) {
		super(context, DBConstants.DATABASE, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DBConstants.SQL_CREATE_MUSIC_HISTORY_TABLE);
		db.execSQL(DBConstants.SQL_CREATE_FINGERPRINTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
	}

}
