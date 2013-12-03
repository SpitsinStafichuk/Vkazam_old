package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;


public class FingerprintDAO extends AbstractDAO {
	
	public FingerprintDAO(Context context) {
		super(context, DBConstants.FINGERPRINTS_TABLE);
	}
	
	@Override
	public synchronized long insert(Data data) {
		DatabaseFingerprintData fingerprintData = (DatabaseFingerprintData) data;
		databaseHelper = new DBHelper(context);
		database = databaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBConstants.FINGERPRINT_DATA, fingerprintData.getFingerprint());
		values.put(DBConstants.DATE, fingerprintData.getDate().getTime());
		long result = database.insert(DBConstants.FINGERPRINTS_TABLE, null, values);
		database.close();
		databaseHelper.close();
		return result;
	}
	
	@Override
	public synchronized int delete(Data data) {
		DatabaseFingerprintData fingerprintData = (DatabaseFingerprintData) data;
		databaseHelper = new DBHelper(context);
		database = databaseHelper.getWritableDatabase();
		int result = database.delete(DBConstants.FINGERPRINTS_TABLE, DBConstants.DATE + "=?", new String[] {"" + fingerprintData.getDate()});
		Log.v("Delete", "Deletion from db is " + result);
		database.close();
		databaseHelper.close();
		return result;
	}

	@Override
	public int update(Data data) {
		return 0;
	}
	
	@Override
	protected List<Data> getListByCursor(Cursor cursor) {
		DatabaseFingerprintData instance;
		List<Data> result = new LinkedList<Data>();
		for(int i = 0; i < cursor.getCount(); i++) {
			instance = new DatabaseFingerprintData(
					Long.parseLong(cursor.getString(cursor.getColumnIndex(DBConstants.ID))),
					this,
					cursor.getString(cursor.getColumnIndex(DBConstants.FINGERPRINT_DATA)),
					new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(DBConstants.DATE)))));
			result.add(instance);
			
			cursor.moveToNext();
		}
		return result;
	}

}
