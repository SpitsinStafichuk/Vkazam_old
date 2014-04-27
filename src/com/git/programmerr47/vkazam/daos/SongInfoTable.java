
package com.git.programmerr47.vkazam.daos;

import android.database.sqlite.SQLiteDatabase;

public class SongInfoTable {

    public static final String SONG_INFO = "SongInfo";

    public static final String _ID = "_id";
    public static final String TRACK__ID = "Track_id";
    public static final String DATE = "Date";
    public static final String SONG_POSITION = "SongPosition";
    public static final String FAVOURITE = "Favourite";
    public static final String DELETION_TIME = "DeletionTime";

    public static void create(SQLiteDatabase database) {
        final String createStatement = "CREATE TABLE " + SONG_INFO + "(" +
                _ID + " INTEGER PRIMARY KEY, " +
                TRACK__ID + " TEXT, " +
                DATE + " INTEGER, " +
                SONG_POSITION + " INTEGER, " +
                DELETION_TIME + " INTEGER, " +
                "FOREIGN KEY(" + TRACK__ID + ") " + "REFERENCES " +
                GracenoteSongInfoTable.GRACENOTE_SONG_INFO + "(" + GracenoteSongInfoTable._ID
                + ")" + ");";
        database.execSQL(createStatement);
    }

}
