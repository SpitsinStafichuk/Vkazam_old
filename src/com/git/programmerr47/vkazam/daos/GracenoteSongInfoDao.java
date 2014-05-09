
package com.git.programmerr47.vkazam.daos;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.git.programmerr47.vkazam.vos.GracenoteSongInfo;
import com.git.programmerr47.vkazam.vos.SongUrl;

public class GracenoteSongInfoDao {

    DatabaseHelper databaseHelper;
    SongUrlDao songUrlDao;

    public GracenoteSongInfoDao(Context context) {
        databaseHelper = new DatabaseHelper(context);
        songUrlDao = new SongUrlDao(context);
    }

    public GracenoteSongInfo get(long id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(GracenoteSongInfoTable.GRACENOTE_SONG_INFO, null,
                GracenoteSongInfoTable._ID + "=?",
                new String[] {
                    Long.toString(id)
                }, null, null, null);
        GracenoteSongInfo gracenoteSongInfo = null;
        if (cursor.moveToFirst()) {
            gracenoteSongInfo = new GracenoteSongInfo(
                    cursor.getInt(cursor.getColumnIndex(GracenoteSongInfoTable._ID)),
                    cursor.getString(cursor.getColumnIndex(GracenoteSongInfoTable.TRACK_ID)),
                    cursor.getString(cursor.getColumnIndex(GracenoteSongInfoTable.ARTIST)),
                    cursor.getString(cursor.getColumnIndex(GracenoteSongInfoTable.ALBUM)),
                    cursor.getString(cursor.getColumnIndex(GracenoteSongInfoTable.TITLE)),
                    cursor.getString(cursor.getColumnIndex(GracenoteSongInfoTable.COVER_ART_URL)),
                    cursor.getString(cursor
                            .getColumnIndex(GracenoteSongInfoTable.ALBUM_RELEASE_YEAR)),
                    cursor.getString(cursor.getColumnIndex(GracenoteSongInfoTable.ALBUM_ARTIST)),
                    cursor.getString(cursor.getColumnIndex(GracenoteSongInfoTable.LYRICS)),
                    new ArrayList<SongUrl>());
        }
        db.close();
        return gracenoteSongInfo;
    }

    public long save(GracenoteSongInfo gracenoteSongInfo) {
        long result;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(GracenoteSongInfoTable.TRACK_ID, gracenoteSongInfo.getTrackId());
            values.put(GracenoteSongInfoTable.ARTIST, gracenoteSongInfo.getArtist());
            values.put(GracenoteSongInfoTable.ALBUM, gracenoteSongInfo.getAlbum());
            values.put(GracenoteSongInfoTable.TITLE, gracenoteSongInfo.getTitle());
            values.put(GracenoteSongInfoTable.COVER_ART_URL, gracenoteSongInfo.getCoverArtUrl());
            values.put(GracenoteSongInfoTable.ALBUM_RELEASE_YEAR,
                    gracenoteSongInfo.getAlbumReleaseYear());
            values.put(GracenoteSongInfoTable.ALBUM_ARTIST, gracenoteSongInfo.getAlbumArtist());
            values.put(GracenoteSongInfoTable.LYRICS, gracenoteSongInfo.getLyrics());
            if (gracenoteSongInfo.getId() == -1) {
                result = db.insert(SongUrlTable.SONG_URL, null, values);
                if (result == -1) {
                    throw new SQLException("Cannot insert gracenoteSongInfo");
                }
            } else {
                result = db.update(SongUrlTable.SONG_URL, values, SongUrlTable._ID
                        + "=?",
                        new String[] {
                            Long.toString(gracenoteSongInfo.getId())
                        });
                if (result != 1) {
                    throw new SQLException("Cannot update gracenoteSongInfo");
                }
            }
            List<SongUrl> songUrls = gracenoteSongInfo.getSongUrls();
            for (SongUrl songUrl : songUrls) {
                long saveResult = songUrlDao.save(songUrl);
                if (songUrl.getId() == -1) {
                    if (saveResult == -1) {
                        throw new SQLException("Cannot insert songUrl");
                    }
                } else {
                    if (saveResult != 1) {
                        throw new SQLException("Cannot update songUrl with id:" + songUrl.getId());
                    }
                }
                if (songUrl.getId() == -1) {
                    ContentValues assignmentValues = new ContentValues();
                    assignmentValues.put(GracenoteSongInfoSongUrlAssignmentTable.TRACK__ID,
                            gracenoteSongInfo.getTrackId());
                    assignmentValues.put(GracenoteSongInfoSongUrlAssignmentTable.URL__ID,
                            saveResult);
                    long assignmentResult = db
                            .insert(GracenoteSongInfoSongUrlAssignmentTable.GRACENOTE_SONG_INFO_SONG_URL_ASSIGNMENT,
                                    null, assignmentValues);
                    if (assignmentResult == -1) {
                        throw new SQLException("Cannot insert gracenoteSongInfoSongUrlAssignment");
                    }
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return result;
    }
}
