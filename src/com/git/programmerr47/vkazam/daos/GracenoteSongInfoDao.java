package com.git.programmerr47.vkazam.daos;

import java.util.ArrayList;
import java.util.List;

import com.git.programmerr47.vkazam.vos.GracenoteSongInfo;
import com.git.programmerr47.vkazam.vos.SongUrl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GracenoteSongInfoDao {

	DatabaseHelper databaseHelper;
	SongUrlDao songUrlDao;

    public GracenoteSongInfoDao(Context context) {
        databaseHelper = new DatabaseHelper(context);
        songUrlDao = new SongUrlDao(context);
    }

    public GracenoteSongInfo get(int id) {
    	SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(GracenoteSongInfoTable.GRACENOTE_SONG_INFO, null, GracenoteSongInfoTable._ID + "=?",
                new String[] {
                    Integer.toString(id)
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
                    cursor.getString(cursor.getColumnIndex(GracenoteSongInfoTable.ALBUM_RELEASE_YEAR)),
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
        ContentValues values = new ContentValues();
        values.put(GracenoteSongInfoTable.TRACK_ID, gracenoteSongInfo.getTrackId());
        values.put(GracenoteSongInfoTable.ARTIST, gracenoteSongInfo.getArtist());
        values.put(GracenoteSongInfoTable.ALBUM, gracenoteSongInfo.getAlbum());
        values.put(GracenoteSongInfoTable.TITLE, gracenoteSongInfo.getTitle());
        values.put(GracenoteSongInfoTable.COVER_ART_URL, gracenoteSongInfo.getCoverArtUrl());
        values.put(GracenoteSongInfoTable.ALBUM_RELEASE_YEAR, gracenoteSongInfo.getAlbumReleaseYear());
        values.put(GracenoteSongInfoTable.ALBUM_ARTIST, gracenoteSongInfo.getAlbumArtist());
        values.put(GracenoteSongInfoTable.LYRICS, gracenoteSongInfo.getLyrics());
        if (gracenoteSongInfo.getId() == -1) {
            result = db.insert(SongUrlTable.SONG_URL, null, values);
        } else {
            result = db.update(SongUrlTable.SONG_URL, values, SongUrlTable._ID
                    + "=?",
                    new String[] {
                        Integer.toString(gracenoteSongInfo.getId())
                    });
        }
        List<SongUrl> songUrls = gracenoteSongInfo.getSongUrls();
        for(SongUrl songUrl : songUrls) {
        	songUrlDao.save(songUrl);
        	//TODO: assignment
        }
        db.close();
        return result;
    }
}
