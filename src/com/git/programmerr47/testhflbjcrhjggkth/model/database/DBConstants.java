package com.git.programmerr47.testhflbjcrhjggkth.model.database;

public interface DBConstants {

	String DATABASE = "history_database.db";
	
	String ID = "_id";
	String DATE = "date";
	
	String MUSIC_HISTORY_TABLE = "history";
	String MUSIC_HISTORY_ARTIST = "artist";
	String MUSIC_HISTORY_TITLE = "title"; 
	String MUSIC_HISTORY_GRACENOTE_TRACK_ID = "song_data_link";
	String MUSIC_HISTORY_PLEERCOM_LINK = "pleercom_link";
	String MUSIC_HISTORY_COVER_ART_URL = "cover_art_url";
	
	String FINGERPRINTS_TABLE = "fingerprints";
	String FINGERPRINT_DATA = "fingerprint_data";
	
	String SQL_CREATE_MUSIC_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ MUSIC_HISTORY_TABLE + " (" + ID + " INTEGER PRIMARY KEY,"
			+ MUSIC_HISTORY_ARTIST + " VARCHAR(100)," 
			+ MUSIC_HISTORY_TITLE + " VARCHAR(100)," 
			+ DATE + " INTEGER," 	
			+ MUSIC_HISTORY_GRACENOTE_TRACK_ID + " VARCHAR(100)," 
			+ MUSIC_HISTORY_PLEERCOM_LINK + " VARCHAR(100)," 
			+ MUSIC_HISTORY_COVER_ART_URL + " TEXT"
			+")";
	
	String SQL_CREATE_FINGERPRINTS_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ FINGERPRINTS_TABLE + " (" + ID + " INTEGER PRIMARY KEY,"
			+ FINGERPRINT_DATA + " TEXT," 
			+ DATE + " INTEGER" 
			+")";
	
	String SQL_DROP_MUSIC_HISTORY_TABLE = "DROP TABLE IF EXISTS " + MUSIC_HISTORY_TABLE;
	
	String SQL_DROP_FINGERPRINTS_TABLE = "DROP TABLE IF EXISTS " + FINGERPRINTS_TABLE;
}
