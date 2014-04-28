
package com.git.programmerr47.vkazam.daos;

import android.database.sqlite.SQLiteDatabase;

public class GracenoteSongInfoTable {

    public static final String GRACENOTE_SONG_INFO = "GracenoteSongInfo";

    public static final String _ID = "_id";
    public static final String TRACK_ID = "TrackId";
    public static final String ARTIST = "Artist";
    public static final String ALBUM = "Album";
    public static final String TITLE = "Title";
    public static final String COVER_ART_URL = "CoverArtUrl";
    public static final String ALBUM_RELEASE_YEAR = "AlbumReleaseYear";
    public static final String ALBUM_ARTIST = "AlbumArtist";
    public static final String LYRICS = "Lyrics";

    public static void create(SQLiteDatabase database) {
        final String createStatement = "CREATE TABLE " + GRACENOTE_SONG_INFO + "(" +
                _ID + " INTEGER PRIMARY KEY, " +
                TRACK_ID + " TEXT UNIQUE, " +
                ARTIST + " TEXT, " +
                TITLE + " TEXT, " +
                COVER_ART_URL + " TEXT, " +
                ALBUM_RELEASE_YEAR + " INTEGER, " +
                ALBUM_ARTIST + " TEXT, " +
                LYRICS + " TEXT)";
        database.execSQL(createStatement);
    }
}
