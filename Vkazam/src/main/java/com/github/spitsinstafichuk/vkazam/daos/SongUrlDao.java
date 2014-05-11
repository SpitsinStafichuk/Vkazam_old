
package com.github.spitsinstafichuk.vkazam.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.spitsinstafichuk.vkazam.vos.SongUrl;

public class SongUrlDao {

    DatabaseHelper databaseHelper;

    public SongUrlDao(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public SongUrl get(long id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        assert db != null;
        Cursor cursor = db.query(SongUrlTable.SONG_URL, null, SongUrlTable._ID + "=?",
                new String[] {
                    Long.toString(id)
                }, null, null, null);
        SongUrl songUrl = null;
        if (cursor.moveToFirst()) {
            songUrl = new SongUrl(
                    cursor.getInt(cursor.getColumnIndex(SongUrlTable._ID)),
                    cursor.getString(cursor.getColumnIndex(SongUrlTable.URL)),
                    cursor.getString(cursor.getColumnIndex(SongUrlTable.ARTIST)),
                    cursor.getString(cursor.getColumnIndex(SongUrlTable.TITLE)),
                    cursor.getInt(cursor.getColumnIndex(SongUrlTable.TYPE)));
        }
        db.close();
        return songUrl;
    }

    public long save(SongUrl songUrl) {
        long result;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        assert db != null;
        ContentValues values = new ContentValues();
        values.put(SongUrlTable.ARTIST, songUrl.getArtist());
        values.put(SongUrlTable.URL, songUrl.getUrl());
        values.put(SongUrlTable.TITLE, songUrl.getTitle());
        values.put(SongUrlTable.TYPE, songUrl.getUrlType());
        if (songUrl.getId() == -1) {
            result = db.insert(SongUrlTable.SONG_URL, null, values);
        } else {
            result = db.update(SongUrlTable.SONG_URL, values, SongUrlTable._ID
                    + "=?",
                    new String[] {
                        Long.toString(songUrl.getId())
                    });
        }
        db.close();
        return result;
    }
}
