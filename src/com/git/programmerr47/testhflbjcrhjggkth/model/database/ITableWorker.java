package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import android.content.ContentValues;
import android.database.Cursor;

public interface ITableWorker {

	//void open();
	//void close();
	
	boolean insert(ContentValues values);
	boolean delete(String key, String[] values);
	Cursor select(String[] names, String[] values);
	Cursor selectAll();
	boolean update(ContentValues targetValues, String key, String[] values);
	Cursor select(ContentValues values);
}
