
package com.git.programmerr47.vkazam.daos;

import android.database.sqlite.SQLiteDatabase;

public class SongUrlTable {

    public static final String SONG_URL = "SongUrl";

    public static final String _ID = "_id";
    public static final String URL = "Url";
    public static final String ARTIST = "Artist";
    public static final String TITLE = "Title";
    public static final String TYPE = "Type";

    public static void create(SQLiteDatabase database) {
        final String createStatement = "CREATE TABLE " + SONG_URL + "(" +
                _ID + " integer primary key, " +
                URL + " text, " +
                ARTIST + " text, " +
                TITLE + " text, " +
                TYPE + " integer)";
        ;
        database.execSQL(createStatement);
    }

}
