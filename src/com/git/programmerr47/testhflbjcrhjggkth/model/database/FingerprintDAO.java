package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.Data;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.FingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;

public class FingerprintDAO extends AbstractDAO {
	
	public FingerprintDAO(Context context) {
		super(context, DBConstants.FINGERPRINTS_TABLE);
		dataSet = new LinkedList<Data>();
	}
	
	@Override
	public synchronized long insert(Data data) {
		FingerprintData fingerprintData = (FingerprintData) data;
		databaseHelper = new DBHelper(context);
		database = databaseHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBConstants.FINGERPRINT_DATA, fingerprintData.getFingerprint());
		values.put(DBConstants.FINGERPRINT_DATE, fingerprintData.getDate());
		long result = database.insert(DBConstants.FINGERPRINTS_TABLE, null, values);
		if(result == 1) {
			dataSet.add(fingerprintData);
		}
		database.close();
		databaseHelper.close();
		return result;
	}
	
	@Override
	public synchronized int delete(Data data) {
		FingerprintData fingerprintData = (FingerprintData) data;
		databaseHelper = new DBHelper(context);
		database = databaseHelper.getWritableDatabase();
		int result = database.delete(DBConstants.FINGERPRINTS_TABLE, DBConstants.FINGERPRINT_DATE + "=?", new String[] {fingerprintData.getDate()});
		Log.v("Delete", "Deletion from db is " + result);
		if(result == 1) {
			dataSet.remove(fingerprintData);
		}
		database.close();
		databaseHelper.close();
		return result;
	}

	@Override
	public int update(Data data) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	protected void mutateListByCursor(Cursor cursor) {
		FingerprintData instance;
		for(int i = 0; i < cursor.getCount(); i++) {
			instance = new FingerprintData.FingerprintDataBuilder()
										.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(DBConstants.FINGERPRINT_ID))))
										.setDate(cursor.getString(cursor.getColumnIndex(DBConstants.FINGERPRINT_DATE)))
										.setFingerprint(cursor.getString(cursor.getColumnIndex(DBConstants.FINGERPRINT_DATA)))
										.build();
			dataSet.add(instance);
			
			cursor.moveToNext();
		}
	}

}
