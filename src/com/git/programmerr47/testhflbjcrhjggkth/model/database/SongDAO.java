package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.Data;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;

public class SongDAO extends AbstractDAO{
	
	public SongDAO(Context context) {
		super(context, DBConstants.MUSIC_HISTORY_TABLE);
		dataSet = new LinkedList<Data>();
	}
	
	@Override
	public synchronized long insert(Data data) {
		SongData songData = (SongData) data;
		databaseHelper = new DBHelper(context);
		database = databaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBConstants.MUSIC_HISTORY_ARTIST, songData.getArtist());
		values.put(DBConstants.MUSIC_HISTORY_TITLE, songData.getTitle());
		values.put(DBConstants.MUSIC_HISTORY_GRACENOTE_TRACK_ID, songData.getTrackId());
		values.put(DBConstants.MUSIC_HISTORY_DATE, songData.getDate());
		values.put(DBConstants.MUSIC_HISTORY_COVER_ART_URL, songData.getCoverArtURL());
		long result = database.insert(DBConstants.MUSIC_HISTORY_TABLE, null, values);
		Log.v("HistoryList", "" + result);
		database.close();
		databaseHelper.close();
		if(result > 0) {
			dataSet.add(songData);
		}
		return result;
	}
	
	@Override
	public synchronized int update(Data data) {
		SongData songData = (SongData) data;
		databaseHelper = new DBHelper(context);
		database = databaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBConstants.MUSIC_HISTORY_PLEERCOM_LINK, songData.getPleercomURL());
		values.put(DBConstants.MUSIC_HISTORY_COVER_ART_URL, songData.getCoverArtURL());
		//TODO подумать над разумностью сравнения по датам
		int result = database.update(DBConstants.MUSIC_HISTORY_TABLE, values, DBConstants.MUSIC_HISTORY_DATE + "=?", new String[] {songData.getDate()});
		database.close();
		databaseHelper.close();
		return result;
	}
	
	@Override
	public synchronized int delete(Data data) {
		SongData songData = (SongData) data;
		databaseHelper = new DBHelper(context);
		database = databaseHelper.getWritableDatabase();
		int result = database.delete(DBConstants.MUSIC_HISTORY_TABLE, DBConstants.MUSIC_HISTORY_DATE + "=?", new String[] {songData.getDate()});
		Log.v("Delete", "Deletion from db is " + result);
		if(result == 1) {
			dataSet.remove(songData);
		}
		database.close();
		databaseHelper.close();
		return result;
	}
}
