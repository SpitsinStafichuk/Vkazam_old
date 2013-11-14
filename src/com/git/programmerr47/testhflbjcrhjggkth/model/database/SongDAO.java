package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.ISongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;

public class SongDAO implements ISongDAO {

	private List<ISongData> songDataSet;
	private HistoryDBHelper historyDBHelper;
	private SQLiteDatabase database;
	private Context context;
	private volatile boolean isFirstGetHistory = true;
	
	public SongDAO(Context context) {
		songDataSet = new LinkedList<ISongData>();
		this.context = context;
	}

	@Override
	public List<ISongData> getHistory() {
		if(isFirstGetHistory) {
			historyDBHelper = new HistoryDBHelper(context);
			database = historyDBHelper.getWritableDatabase();
			Cursor cursor = database.rawQuery("SELECT * FROM " + DBConstants.MUSIC_HISTORY_TABLE, null);
			cursor.moveToFirst();
			List<ISongData> result = getListByCursor(cursor);
			database.close();
			historyDBHelper.close();
			isFirstGetHistory = false;
			return result;
		} else {
			return songDataSet;
		}
	}
	
	@Override
	public synchronized long insert(ISongData songData) {
		historyDBHelper = new HistoryDBHelper(context);
		database = historyDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBConstants.MUSIC_HISTORY_ARTIST, songData.getArtist());
		values.put(DBConstants.MUSIC_HISTORY_TITLE, songData.getTitle());
		values.put(DBConstants.MUSIC_HISTORY_GRACENOTE_TRACK_ID, songData.getTrackId());
		values.put(DBConstants.MUSIC_HISTORY_DATE, songData.getDate());
		long result = database.insert(DBConstants.MUSIC_HISTORY_TABLE, null, values);
		database.close();
		historyDBHelper.close();
		if(result == 1) {
			songDataSet.add(songData);
		}
		return result;
	}
	
	@Override
	public synchronized int update(ISongData songData) {
		historyDBHelper = new HistoryDBHelper(context);
		database = historyDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBConstants.MUSIC_PLEERCOM_LINK, songData.getPleercomURL());
		//TODO подумать над разумностью сравнения по датам
		int result = database.update(DBConstants.MUSIC_HISTORY_TABLE, values, DBConstants.MUSIC_HISTORY_DATE + "=?", new String[] {songData.getDate()});
		database.close();
		historyDBHelper.close();
		if(result == 1) {
			songDataSet.remove(songData);
			songDataSet.add(songData);
		}
		return result;
	}
	
	@Override
	public synchronized int delete(ISongData songData) {
		historyDBHelper = new HistoryDBHelper(context);
		database = historyDBHelper.getWritableDatabase();
		int result = database.delete(DBConstants.MUSIC_HISTORY_TABLE, DBConstants.MUSIC_HISTORY_DATE + "=?", new String[] {songData.getDate()});
		Log.v("Delete", "Deletion from db is " + result);
		if(result == 1) {
			songDataSet.remove(songData);
		}
		database.close();
		historyDBHelper.close();
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
