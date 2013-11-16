package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import android.content.ContentValues;
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
		ContentValues values = new ContentValues();
		values.put(DBConstants.MUSIC_HISTORY_ARTIST, "Test1");
		values.put(DBConstants.MUSIC_HISTORY_TITLE, "Test1");
		values.put(DBConstants.MUSIC_HISTORY_GRACENOTE_TRACK_ID, "Test1");
		values.put(DBConstants.MUSIC_HISTORY_DATE, "Date1");
		db.insert(DBConstants.MUSIC_HISTORY_TABLE, null, values);
		values = new ContentValues();
		values.put(DBConstants.MUSIC_HISTORY_ARTIST, "Test2");
		values.put(DBConstants.MUSIC_HISTORY_TITLE, "Test2");
		values.put(DBConstants.MUSIC_HISTORY_GRACENOTE_TRACK_ID, "Test2");
		values.put(DBConstants.MUSIC_HISTORY_DATE, "Date2");
		db.insert(DBConstants.MUSIC_HISTORY_TABLE, null, values);
		values = new ContentValues();
		values.put(DBConstants.MUSIC_HISTORY_ARTIST, "Test3");
		values.put(DBConstants.MUSIC_HISTORY_TITLE, "Test3");
		values.put(DBConstants.MUSIC_HISTORY_GRACENOTE_TRACK_ID, "Test3");
		values.put(DBConstants.MUSIC_HISTORY_DATE, "Date3");
		db.insert(DBConstants.MUSIC_HISTORY_TABLE, null, values);
		values = new ContentValues();
		values.put(DBConstants.MUSIC_HISTORY_ARTIST, "Test4");
		values.put(DBConstants.MUSIC_HISTORY_TITLE, "Test4");
		values.put(DBConstants.MUSIC_HISTORY_GRACENOTE_TRACK_ID, "Test4");
		values.put(DBConstants.MUSIC_HISTORY_DATE, "Date4");
		db.insert(DBConstants.MUSIC_HISTORY_TABLE, null, values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DBConstants.SQL_DROP_FINGERPRINTS_TABLE);
		db.execSQL(DBConstants.SQL_DROP_MUSIC_HISTORY_TABLE);
		onCreate(db);
	}

}
