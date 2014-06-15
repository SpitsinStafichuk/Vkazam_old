
package com.github.spitsinstafichuk.vkazam.daos;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.github.spitsinstafichuk.vkazam.vos.GracenoteSongInfo;
import com.github.spitsinstafichuk.vkazam.vos.SongUrl;

public class GracenoteSongInfoDao {

    DatabaseHelper databaseHelper;

    SongUrlDao songUrlDao;

    public GracenoteSongInfoDao(Context context) {
        databaseHelper = new DatabaseHelper(context);
        songUrlDao = new SongUrlDao(context);
    }

    public GracenoteSongInfo get(long id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.query(GracenoteSongInfoTable.GRACENOTE_SONG_INFO, null,
                GracenoteSongInfoTable._ID + "=?",
                new String[]{Long.toString(id)}, null, null, null);
        GracenoteSongInfo gracenoteSongInfo = null;
        if (cursor.moveToFirst()) {
            Cursor songUrlCursor = db.rawQuery("SELECT * FROM ? INNER JOIN ? ON ?=? WHERE ?=?",
                    new String[]{
                            GracenoteSongInfoSongUrlAssignmentTable.GRACENOTE_SONG_INFO_SONG_URL_ASSIGNMENT,
                            SongUrlTable.SONG_URL, GracenoteSongInfoSongUrlAssignmentTable.URL__ID,
                            SongUrlTable._ID, GracenoteSongInfoSongUrlAssignmentTable.TRACK__ID,
                            Long.toString(id)});
            List<SongUrl> songUrls = new ArrayList<SongUrl>();
            while (cursor.moveToNext()) {
                SongUrl songUrl = new SongUrl(
                        songUrlCursor.getInt(cursor.getColumnIndex(SongUrlTable._ID)),
                        songUrlCursor.getString(cursor.getColumnIndex(SongUrlTable.URL)),
                        songUrlCursor.getString(cursor.getColumnIndex(SongUrlTable.ARTIST)),
                        songUrlCursor.getString(cursor.getColumnIndex(SongUrlTable.TITLE)),
                        songUrlCursor.getInt(cursor.getColumnIndex(SongUrlTable.TYPE)));
                songUrls.add(songUrl);
            }
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
                    songUrls);
        }
        db.close();
        return gracenoteSongInfo;
    }

    public long save(GracenoteSongInfo gracenoteSongInfo) {
        long result;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        if (db == null) {
            if (gracenoteSongInfo.getId() == -1) {
                return -1;
            } else {
                return 0;
            }
        }
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(GracenoteSongInfoTable.TRACK_ID, gracenoteSongInfo.getTrackId());
            values.put(GracenoteSongInfoTable.ARTIST, gracenoteSongInfo.getArtist());
            values.put(GracenoteSongInfoTable.ALBUM, gracenoteSongInfo.getAlbum());
            values.put(GracenoteSongInfoTable.TITLE, gracenoteSongInfo.getTitle());
            values.put(GracenoteSongInfoTable.COVER_ART_URL,
                    gracenoteSongInfo.getCoverArtUrl());
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
                        new String[]{
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
                        throw new SQLException(
                                "Cannot update songUrl with id:" + songUrl.getId());
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
                        throw new SQLException(
                                "Cannot insert gracenoteSongInfoSongUrlAssignment");
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
