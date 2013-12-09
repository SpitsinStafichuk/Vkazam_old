package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class SongDAO extends AbstractDAO {
	
	public SongDAO(Context context) {
		super(context, DBConstants.MUSIC_HISTORY_TABLE);
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
		if(songData.getCoverArtUrl() != null) values.put(DBConstants.MUSIC_HISTORY_COVER_ART_URL, songData.getCoverArtUrl());
		if(songData.getPleercomUrl() != null) values.put(DBConstants.MUSIC_HISTORY_PLEERCOM_URL, songData.getPleercomUrl());
		if(songData.getPleercomUrl() != null) values.put(DBConstants.MUSIC_HISTORY_PLEERCOM_URL, songData.getPleercomUrl());
		if(songData.getContributorImageUrl() != null) values.put(DBConstants.MUSIC_HISTORY_CONTRIBUTOR_IMAGE_URL, songData.getContributorImageUrl());
		if(songData.getArtistBiographyURL() != null) values.put(DBConstants.MUSIC_HISTORY_BIOGRAPHY_URL, songData.getArtistBiographyURL());
		if(songData.getAlbumReviewUrl() != null) values.put(DBConstants.MUSIC_HISTORY_ALBUM_REVIEW_URL, songData.getAlbumReviewUrl());
		if(songData.getAlbumReleaseYear() != null) values.put(DBConstants.MUSIC_HISTORY_ALBUM_RELEASE_YEAR, songData.getAlbumReleaseYear());
		if(songData.getAlbumArtist() != null) values.put(DBConstants.MUSIC_HISTORY_ALBUM_ARTIST, songData.getAlbumArtist());
		long result = database.insert(DBConstants.MUSIC_HISTORY_TABLE, null, values);
		Log.v("HistoryList", "" + result);
		database.close();
		databaseHelper.close();
		return result;
	}
	
	@Override
	public synchronized int update(Data data) {
		DatabaseSongData songData = (DatabaseSongData) data;
		databaseHelper = new DBHelper(context);
		database = databaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
        Log.v("Database", "" + songData.getPleercomUrl());
		if(songData.getCoverArtUrl() != null) values.put(DBConstants.MUSIC_HISTORY_COVER_ART_URL, songData.getCoverArtUrl());
		if(songData.getPleercomUrl() != null) values.put(DBConstants.MUSIC_HISTORY_PLEERCOM_URL, songData.getPleercomUrl());
		if(songData.getVkAudioId() != null) values.put(DBConstants.MUSIC_HISTORY_PLEERCOM_URL, songData.getVkAudioId());
		if(songData.getContributorImageUrl() != null) values.put(DBConstants.MUSIC_HISTORY_CONTRIBUTOR_IMAGE_URL, songData.getContributorImageUrl());
		if(songData.getArtistBiographyURL() != null) values.put(DBConstants.MUSIC_HISTORY_BIOGRAPHY_URL, songData.getArtistBiographyURL());
		if(songData.getAlbumReviewUrl() != null) values.put(DBConstants.MUSIC_HISTORY_ALBUM_REVIEW_URL, songData.getAlbumReviewUrl());
		if(songData.getAlbumReleaseYear() != null) values.put(DBConstants.MUSIC_HISTORY_ALBUM_RELEASE_YEAR, songData.getAlbumReleaseYear());
		if(songData.getAlbumArtist() != null) values.put(DBConstants.MUSIC_HISTORY_ALBUM_ARTIST, songData.getAlbumArtist());
		//TODO подумать над разумностью сравнения по датам
		int result = database.update(DBConstants.MUSIC_HISTORY_TABLE, values, DBConstants.DATE + "=?", new String[] {"" + songData.getDate().getTime()});
        Log.v("Database", "" + result);
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
		database.close();
		databaseHelper.close();
		return result;
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
					cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_ALBUM)),
					cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_TITLE)),
					new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(DBConstants.DATE)))),
					cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_PLEERCOM_URL)),
					cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_VK_AUDIO_ID)),
					cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_COVER_ART_URL)),
					cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_CONTRIBUTOR_IMAGE_URL)),
					cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_BIOGRAPHY_URL)),
					cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_ALBUM_REVIEW_URL)),
					cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_ALBUM_RELEASE_YEAR)),
					cursor.getString(cursor.getColumnIndex(DBConstants.MUSIC_HISTORY_ALBUM_ARTIST)));
			result.add(instance);
			cursor.moveToNext();
		}
		return result;
	}

}
