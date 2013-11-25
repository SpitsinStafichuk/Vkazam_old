package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISongDAOObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISongDAOObserver;

//TODO перенести ISongDAOObservable в SongList
public class SongDAO extends AbstractDAO implements ISongDAOObservable {
	private Set<ISongDAOObserver> songDAOObservers;
	
	public SongDAO(Context context) {
		super(context, DBConstants.MUSIC_HISTORY_TABLE);
		songDAOObservers = new HashSet<ISongDAOObserver>();
	}
	
	@Override
	public synchronized long insert(Data data) {
		DatabaseSongData songData = (DatabaseSongData) data;
		databaseHelper = new DBHelper(context);
		database = databaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBConstants.MUSIC_HISTORY_ARTIST, songData.getArtist());
		values.put(DBConstants.MUSIC_HISTORY_TITLE, songData.getTitle());
		values.put(DBConstants.MUSIC_HISTORY_GRACENOTE_TRACK_ID, songData.getTrackId());
		values.put(DBConstants.DATE, songData.getDate().getTime());
		values.put(DBConstants.MUSIC_HISTORY_COVER_ART_URL, songData.getCoverArtUrl());
		long result = database.insert(DBConstants.MUSIC_HISTORY_TABLE, null, values);
		Log.v("HistoryList", "" + result);
		database.close();
		databaseHelper.close();
		if(result > 0) {
			notifySongDAOObservers();
		}
		return result;
	}
	
	@Override
	public synchronized int update(Data data) {
		DatabaseSongData songData = (DatabaseSongData) data;
		databaseHelper = new DBHelper(context);
		database = databaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBConstants.MUSIC_HISTORY_PLEERCOM_LINK, songData.getPleercomUrl());
		values.put(DBConstants.MUSIC_HISTORY_COVER_ART_URL, songData.getCoverArtUrl());
		//TODO подумать над разумностью сравнения по датам
		int result = database.update(DBConstants.MUSIC_HISTORY_TABLE, values, DBConstants.DATE + "=?", new String[] {"" + songData.getDate()});
		database.close();
		databaseHelper.close();
		return result;
	}
	
	@Override
	public synchronized int delete(Data data) {
		DatabaseSongData songData = (DatabaseSongData) data;
		databaseHelper = new DBHelper(context);
		database = databaseHelper.getWritableDatabase();
		int result = database.delete(DBConstants.MUSIC_HISTORY_TABLE, DBConstants.DATE + "=?", new String[] {"" + songData.getDate()});
		Log.v("Delete", "Deletion from db is " + result);
		if(result == 1) {
			notifySongDAOObservers();
		}
		database.close();
		databaseHelper.close();
		return result;
	}
	@Override
	public void addObserver(ISongDAOObserver o) {
		songDAOObservers.add(o);
	}

	@Override
	public void removeObserver(ISongDAOObserver o) {
		songDAOObservers.remove(o);
	}

	@Override
	public void notifySongDAOObservers() {
		for (ISongDAOObserver o : songDAOObservers)
			o.onHistoryListChanged();
	}
	
	@Override
	protected List<Data> getListByCursor(Cursor cursor) {
		DatabaseSongData instance;
		List<Data> result = new LinkedList<Data>();
		for(int i = 0; i < cursor.getCount(); i++) {
			instance = new DatabaseSongData(
					Long.parseLong(cursor.getString(cursor.getColumnIndex(DBConstants.ID))),
					this,
					cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_GRACENOTE_TRACK_ID)),
					cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_ARTIST)),
					cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_TITLE)),
					new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(DBConstants.DATE)))),
					cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_PLEERCOM_LINK)),
					cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_COVER_ART_URL)));
			result.add(instance);
			cursor.moveToNext();
		}
		return result;
	}

}
