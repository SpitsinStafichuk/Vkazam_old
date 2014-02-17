package com.git.programmerr47.vkazam.model.database;


import java.util.HashSet;
import java.util.Set;

import com.git.programmerr47.vkazam.model.SongData;
import com.git.programmerr47.vkazam.model.observers.ISongDAOObservable;
import com.git.programmerr47.vkazam.model.observers.ISongDAOObserver;

import android.content.Context;
import com.git.programmerr47.vkazam.model.observers.ISongDAOObservable;
import com.git.programmerr47.vkazam.model.observers.ISongDAOObserver;

public class SongList extends DatabaseList implements ISongDAOObservable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7599946891053671003L;
	
	private Set<ISongDAOObserver> observers;

	public SongList(Context context) {
		super(new SongDAO(context));
		observers = new HashSet<ISongDAOObserver>();
	}

	public synchronized DatabaseSongData add(SongData songData) {
		DatabaseSongData databaseSongData = new DatabaseSongData(size() + 1 , dao, songData);
		boolean result = super.add(databaseSongData);
		if (result) {
			notifySongDAOObservers();
		}
		return result ? databaseSongData : null;
	}
	
	public synchronized DatabaseSongData add(int index, SongData songData) {
		DatabaseSongData databaseSongData = new DatabaseSongData(size() + 1 , dao, songData);
		super.add(index, databaseSongData);
		notifySongDAOObservers();
		return databaseSongData;
	}
	
	@Override
	public synchronized boolean remove(Object obj) {
		boolean result = super.remove(obj);
		if (result) {
			notifySongDAOObservers();
		}
		return result;
	}

	@Override
	public void addObserver(ISongDAOObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(ISongDAOObserver o) {
		observers.remove(o);
	}

	@Override
	public void notifySongDAOObservers() {
		for(ISongDAOObserver o : observers) 
			o.onHistoryListChanged();
	}
}
