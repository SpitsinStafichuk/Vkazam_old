
package com.git.programmerr47.vkazam.daos;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.git.programmerr47.vkazam.vos.Fingerprint;

public class FingerprintDao {

    DatabaseHelper databaseHelper;

    public FingerprintDao(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public Fingerprint get(int id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(FingerprintTable.FINGERPRINTS, null, FingerprintTable._ID + "=?",
                new String[] {
                    Integer.toString(id)
                }, null, null, null);
        Fingerprint fingerprint = null;
        if (cursor.moveToFirst()) {
            fingerprint = new Fingerprint(
                    cursor.getInt(cursor.getColumnIndex(FingerprintTable._ID)),
                    cursor.getString(cursor.getColumnIndex(FingerprintTable.FINGERPRINT)),
                    new Date(
                            cursor.getLong(cursor.getColumnIndex(FingerprintTable.DATE))),
                    new Date(cursor.getLong(cursor.getColumnIndex(FingerprintTable.DELETION_DATE))));
        }
        db.close();
        return fingerprint;
    }

    public Fingerprint getLastFingerprint() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(FingerprintTable.FINGERPRINTS, null, null,
                null, null, FingerprintTable.DELETION_DATE + " DESC", "1");
        Fingerprint fingerprint = null;
        if (cursor.moveToFirst()) {
            fingerprint = new Fingerprint(
                    cursor.getInt(cursor.getColumnIndex(FingerprintTable._ID)),
                    cursor.getString(cursor.getColumnIndex(FingerprintTable.FINGERPRINT)),
                    new Date(
                            cursor.getLong(cursor.getColumnIndex(FingerprintTable.DATE))),
                    new Date(cursor.getLong(cursor.getColumnIndex(FingerprintTable.DELETION_DATE))));
        }
        db.close();
        return fingerprint;
    }

    public long save(Fingerprint fingerprint) {
        long result;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FingerprintTable.FINGERPRINT, fingerprint.getFingerprint());
        values.put(FingerprintTable.DATE, fingerprint.getDate().getTime());
        values.put(FingerprintTable.DELETION_DATE, fingerprint.getDeletionDate().getTime());
        if (fingerprint.getId() == -1) {
            result = db.insert(FingerprintTable.FINGERPRINTS, null, values);
        } else {
            result = db.update(FingerprintTable.FINGERPRINTS, values, FingerprintTable._ID
                    + "=?",
                    new String[] {
                        Integer.toString(fingerprint.getId())
                    });
        }
        db.close();
        return result;
    }

    public int delete(int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int result = db.delete(FingerprintTable.FINGERPRINTS, FingerprintTable._ID + "=?",
                new String[] {
                    Integer.toString(id)
                });
        db.close();
        return result;
    }

}
