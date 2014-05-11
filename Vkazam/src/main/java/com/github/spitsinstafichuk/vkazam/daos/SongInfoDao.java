
package com.github.spitsinstafichuk.vkazam.daos;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.github.spitsinstafichuk.vkazam.vos.GracenoteSongInfo;
import com.github.spitsinstafichuk.vkazam.vos.SongInfo;

@SuppressWarnings("unused")
public class SongInfoDao {

    DatabaseHelper databaseHelper;
    GracenoteSongInfoDao gracenoteSongInfoDao;

    public SongInfoDao(Context context) {
        databaseHelper = new DatabaseHelper(context);
        gracenoteSongInfoDao = new GracenoteSongInfoDao(context);
    }

    public SongInfo get(long id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        assert db != null;
        Cursor cursor = db.query(SongInfoTable.SONG_INFO, null,
                SongInfoTable._ID + "=?",
                new String[] {
                    Long.toString(id)
                }, null, null, null);
        SongInfo songInfo = null;
        if (cursor.moveToFirst()) {
            GracenoteSongInfo gracenoteSongInfo = gracenoteSongInfoDao.get(cursor.getInt(cursor
                    .getColumnIndex(SongInfoTable.TRACK__ID)));
            songInfo = new SongInfo(
                    gracenoteSongInfo,
                    new Date(cursor.getLong(cursor.getColumnIndex(SongInfoTable.DATE))),
                    cursor.getString(cursor.getColumnIndex(SongInfoTable.SONG_POSITION)),
                    cursor.getInt(cursor.getColumnIndex(SongInfoTable.FAVOURITE)) > 0,
                    new Date(cursor.getLong(cursor.getColumnIndex(SongInfoTable.DELETION_TIME))));
        }

        return songInfo;
    }

    public long save(SongInfo songInfo) {
        long result;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        assert db != null;
        db.beginTransaction();
        try {

            result = gracenoteSongInfoDao.save(songInfo.getGracenoteSongInfo());
            if (songInfo.getGracenoteSongInfo().getId() == -1) {

            }
            ContentValues values = new ContentValues();
            values.put(SongInfoTable.DATE, songInfo.getDate().getTime());
            values.put(SongInfoTable.SONG_POSITION, songInfo.getSongPosition());
            values.put(SongInfoTable.FAVOURITE, songInfo.isFavourite() ? 1 : 0);
            values.put(SongInfoTable.DELETION_TIME, songInfo.getDeletionDate().getTime());
            if (songInfo.getId() == -1) {
                result = db.insert(SongInfoTable.SONG_INFO, null, values);
                if (result == -1) {
                    throw new SQLException("Cannot insert songInfo");
                }
            } else {
                result = db.update(SongInfoTable.SONG_INFO, values, SongInfoTable._ID + "=?",
                        new String[] {
                            Long.toString(songInfo.getId())
                        });
                if (result != 1) {
                    throw new SQLException("Cannot update songInfo");
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
