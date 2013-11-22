package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.Data;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;

public abstract class AbstractDAO {
	
	protected List<Data> dataSet;
	protected String tableName;
	protected DBHelper databaseHelper;
	protected SQLiteDatabase database;
	protected Context context;
	protected volatile boolean isFirstGetSet = true;
	
	public AbstractDAO(Context context, String tableName) {
		this.context = context;
		this.tableName = tableName;
	}
	
	public List<Data> getHistory() {
		if(isFirstGetSet) {
			databaseHelper = new DBHelper(context);
			database = databaseHelper.getWritableDatabase();
			Cursor cursor = database.rawQuery("SELECT * FROM " + tableName, null);
			cursor.moveToFirst();
			mutateListByCursor(cursor);
			database.close();
			databaseHelper.close();
			isFirstGetSet = false;
		}
		Log.v(DBHelper.HISTORY_TAG, "songDataList.size() = " + dataSet.size());
		
		return dataSet;
	}
	
	protected abstract void mutateListByCursor(Cursor cursor);
	
	public abstract long insert(Data data);
	
	public abstract int update(Data data);
	
	public abstract int delete(Data data);
}
