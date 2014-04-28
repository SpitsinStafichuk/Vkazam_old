
package com.git.programmerr47.vkazam.daos;

import android.database.sqlite.SQLiteDatabase;

public class GracenoteSongInfoSongUrlAssignmentTable {

    public static final String GRACENOTE_SONG_INFO_SONG_URL_ASSIGNMENT = "GracenoteSongInfoSongUrlAssignment";

    public static final String TRACK__ID = "Track_id";
    public static final String URL__ID = "Url_id";

    public static void create(SQLiteDatabase database) {
        final String createStatement = "CREATE TABLE "
                + GRACENOTE_SONG_INFO_SONG_URL_ASSIGNMENT + "(" +
                TRACK__ID + " INTEGER, " +
                URL__ID + " INTEGER, " +
                "PRIMARY_KEY" + "(" +
                TRACK__ID + ", " +
                URL__ID + "), " +
                "FOREIGN KEY(" + TRACK__ID + ") " + "REFERENCES " +
                GracenoteSongInfoTable.GRACENOTE_SONG_INFO + "(" + GracenoteSongInfoTable._ID
                + "), "
                +
                "FOREIGN KEY(" + URL__ID + ") " + "REFERENCES " +
                SongUrlTable.SONG_URL + "(" + SongUrlTable._ID + ")" +
                ")";
        database.execSQL(createStatement);
    }
}
