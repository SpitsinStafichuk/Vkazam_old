package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.FingerprintData;

public class FingerprintDAO implements IFingerprintDAO {
	
	List<FingerprintData> fingerprintDataSet;
	HistoryDBHelper historyDBHelper;
	SQLiteDatabase database;
	Context context;
	private volatile boolean isFirstGetFingerprints = true;
	
	public FingerprintDAO(Context context) {
		fingerprintDataSet = new LinkedList<FingerprintData>();
		this.context = context;
	}

	@Override
	public List<FingerprintData> getFingerprints() {
		if(isFirstGetFingerprints) {
			historyDBHelper = new HistoryDBHelper(context);
			database = historyDBHelper.getWritableDatabase();
			Cursor cursor = database.rawQuery("SELECT * FROM " + DBConstants.FINGERPRINTS_TABLE, null);
			cursor.moveToFirst();
			List<FingerprintData> result = getListByCursor(cursor);
			database.close();
			historyDBHelper.close();
			isFirstGetFingerprints = false;
			return result;
		} else {
			return fingerprintDataSet;
		}
	}
	
	@Override
	public synchronized long insert(FingerprintData fingerprintData) {
		historyDBHelper = new HistoryDBHelper(context);
		database = historyDBHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBConstants.FINGERPRINT, fingerprintData.getFingerprint());
		values.put(DBConstants.FINGERPRINT_DATE, fingerprintData.getDate());
		long result = database.insert(DBConstants.FINGERPRINTS_TABLE, null, values);
		if(result == 1) {
			fingerprintDataSet.add(fingerprintData);
		}
		database.close();
		historyDBHelper.close();
		return result;
	}
	
	@Override
	public synchronized int delete(FingerprintData fingerprintData) {
		historyDBHelper = new HistoryDBHelper(context);
		database = historyDBHelper.getWritableDatabase();
		int result = database.delete(DBConstants.FINGERPRINTS_TABLE, DBConstants.FINGERPRINT_DATE + "=?", new String[] {fingerprintData.getDate()});
		Log.v("Delete", "Deletion from db is " + result);
		if(result == 1) {
			fingerprintDataSet.remove(fingerprintData);
		}
		database.close();
		historyDBHelper.close();
		return result;
	}
	
	private List<FingerprintData> getListByCursor(Cursor cursor) {
		List<FingerprintData> fingerprintDataList = new ArrayList<FingerprintData>();
		FingerprintData instance;
		for(int i = 0; i < cursor.getCount(); i++) {
			instance = new FingerprintData.FingerprintDataBuilder()
											.setId(Long.parseLong(cursor.getString(cursor.getColumnIndex(DBConstants.FINGERPRINT_ID))))
											.setFingerprint(cursor.getString(cursor.getColumnIndex(DBConstants.FINGERPRINT)))
											.setDate(cursor.getString(cursor.getColumnIndex(DBConstants.FINGERPRINT_DATE)))
											.build();
			fingerprintDataList.add(instance);
			fingerprintDataSet.add(instance);
			
			cursor.moveToNext();
		}
		
		return fingerprintDataList;
	}

}
