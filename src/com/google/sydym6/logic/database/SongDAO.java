package com.google.sydym6.logic.database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.sydym6.logic.database.data.ISongData;
import com.google.sydym6.logic.database.data.SongData;

public class SongDAO implements ISongDAO {

	Set<ISongData> songDataSet;
	HistoryDBHelper historyDBHelper;
	SQLiteDatabase database;
	
	public SongDAO(Context context) {
		songDataSet = new HashSet<ISongData>();
		historyDBHelper = new HistoryDBHelper(context);
		database = historyDBHelper.getWritableDatabase();
	}

	@Override
	public List<ISongData> getHistory() {
		Cursor cursor = database.rawQuery("SELECT * FROM " + DBConstants.MUSIC_HISTORY_TABLE, null);
		cursor.moveToFirst();
		
		return getListByCursor(cursor);
	}
	

	@Override
	public void insert(ISongData songData) {
		ContentValues values = new ContentValues();
		values.put(DBConstants.MUSIC_HISTORY_ARTIST, songData.getArtist());
		values.put(DBConstants.MUSIC_HISTORY_TITLE, songData.getTitle());
		values.put(DBConstants.MUSIC_HISTORY_GRACENOTE_TRACK_ID, songData.getTrackId());
		values.put(DBConstants.MUSIC_HISTORY_DATE, songData.getDate());
		database.insert(DBConstants.MUSIC_HISTORY_TABLE, null, values);
		songDataSet.add(songData);
	}
	
	@Override
	public int delete(ISongData songData) {
		int result = database.delete(DBConstants.MUSIC_HISTORY_TABLE, DBConstants.MUSIC_HISTORY_DATE + "=?", new String[] {songData.getDate()});
		Log.v("Delete", "Deletion from db is " + result);
		songDataSet.remove(songData);
		return result;
	}
	
	private List<ISongData> getListByCursor(Cursor cursor) {
		List<ISongData> songDataList = new ArrayList<ISongData>();
		ISongData instance;
		for(int i = 0; i < cursor.getCount(); i++) {
			instance = new SongData(Long.parseLong(cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_ID))),
	                				cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_ARTIST)), 
					                cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_TITLE)), 
					                cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_GRACENOTE_TRACK_ID)), 
					                cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_DATE)),
					                cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_PLEERCOM_LINK)));
			
			songDataList.add(instance);
			songDataSet.add(instance);
			
			cursor.moveToNext();
		}
		
		return songDataList;
	}

}
