package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public abstract class AbstractDAO {
	
	protected String tableName;
	protected DBHelper databaseHelper;
	protected SQLiteDatabase database;
	protected Context context;
	protected volatile boolean isFirstGetSet = true;
	
	public AbstractDAO(Context context, String tableName) {
		this.context = context;
		this.tableName = tableName;
	}
	
	List<Data> getHistory() {
		databaseHelper = new DBHelper(context);
		database = databaseHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("SELECT * FROM " + tableName + " ORDER BY " + DBConstants.DATE, null);
		cursor.moveToFirst();
		List<Data> result = getListByCursor(cursor);
		database.close();
		databaseHelper.close();
		isFirstGetSet = false;
		Log.v(DBHelper.HISTORY_TAG, "songDataList.size() = " + result.size());
		
		return result;
	}
	
	protected abstract List<Data> getListByCursor(Cursor cursor);
	
	public abstract long insert(Data data);
	
	public abstract int update(Data data);
	
	public abstract int delete(Data data);
}
