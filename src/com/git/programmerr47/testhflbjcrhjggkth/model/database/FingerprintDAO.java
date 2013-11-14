package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.IFingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.FingerprintData;

public class FingerprintDAO implements IFingerprintDAO {
	
	List<IFingerprintData> fingerprintDataSet;
	HistoryDBHelper historyDBHelper;
	SQLiteDatabase database;
	Context context;
	private volatile boolean isFirstGetFingerprints = true;
	
	public FingerprintDAO(Context context) {
		fingerprintDataSet = new LinkedList<IFingerprintData>();
		this.context = context;
	}

	@Override
	public List<IFingerprintData> getFingerprints() {
		if(isFirstGetFingerprints) {
			historyDBHelper = new HistoryDBHelper(context);
			database = historyDBHelper.getWritableDatabase();
			Cursor cursor = database.rawQuery("SELECT * FROM " + DBConstants.FINGERPRINTS_TABLE, null);
			cursor.moveToFirst();
			List<IFingerprintData> result = getListByCursor(cursor);
			database.close();
			historyDBHelper.close();
			isFirstGetFingerprints = false;
			return result;
		} else {
			return fingerprintDataSet;
		}
	}
	
	@Override
	public synchronized long insert(IFingerprintData fingerprintData) {
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
	public synchronized int delete(IFingerprintData fingerprintData) {
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
	
	private List<IFingerprintData> getListByCursor(Cursor cursor) {
		List<IFingerprintData> fingerprintDataList = new ArrayList<IFingerprintData>();
		IFingerprintData instance;
		for(int i = 0; i < cursor.getCount(); i++) {
			instance = new FingerprintData(Long.parseLong(cursor.getString(cursor.getColumnIndex(DBConstants.FINGERPRINT_ID))),
	                				cursor.getString(cursor.getColumnIndex(DBConstants.FINGERPRINT)), 
					                cursor.getString(cursor.getColumnIndex(DBConstants.FINGERPRINT_DATE)));
			
			fingerprintDataList.add(instance);
			fingerprintDataSet.add(instance);
			
			cursor.moveToNext();
		}
		
		return fingerprintDataList;
	}

}
