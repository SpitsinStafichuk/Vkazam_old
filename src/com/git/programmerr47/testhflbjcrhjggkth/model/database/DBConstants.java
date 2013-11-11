package com.git.programmerr47.testhflbjcrhjggkth.model.database;

public interface DBConstants {

	public static final String DATABASE = "history_database.db";
	public static final String MUSIC_HISTORY_TABLE = "history";
	
	public static final String MUSIC_HISTORY_ID = "_id";
	public static final String MUSIC_HISTORY_ARTIST = "artist";
	public static final String MUSIC_HISTORY_TITLE = "title"; 
	public static final String MUSIC_HISTORY_DATE = "date";
	public static final String MUSIC_HISTORY_GRACENOTE_TRACK_ID = "song_data_link";
	public static final String MUSIC_PLEERCOM_LINK = "pleercom_link";
	
	public static final String FINGERPRINTS_TABLE = "fingerprints";
	
	public static final String FINGERPRINT_ID = "_id";
	public static final String FINGERPRINT = "fingerprint_data";
	public static final String FINGERPRINT_DATE = "fingerprint_date"; 
	
	String SQL_CREATE_MUSIC_HISTORY_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ MUSIC_HISTORY_TABLE + " (" + MUSIC_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ MUSIC_HISTORY_ARTIST + " VARCHAR(100)," 
			+ MUSIC_HISTORY_TITLE + " VARCHAR(100)," 
			+ MUSIC_HISTORY_DATE + " VARCHAR(100)," 	
			+ MUSIC_HISTORY_GRACENOTE_TRACK_ID + " VARCHAR(100)," 
			+ MUSIC_PLEERCOM_LINK + " VARCHAR(100)" 
			+")";
	
	String SQL_CREATE_FINGERPRINTS_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ FINGERPRINTS_TABLE + " (" + FINGERPRINT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ FINGERPRINT + " VARCHAR(100)," 
			+ FINGERPRINT_DATE + " VARCHAR(100)" 
			+")";

}
