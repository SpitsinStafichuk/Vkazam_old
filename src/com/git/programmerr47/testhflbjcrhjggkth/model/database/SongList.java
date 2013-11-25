package com.git.programmerr47.testhflbjcrhjggkth.model.database;


import java.util.HashSet;
import java.util.Set;

import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISongDAOObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISongDAOObserver;

import android.content.Context;

public class SongList extends DatabaseList implements ISongDAOObservable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7599946891053671003L;
	
	private Set<ISongDAOObserver> observers;

	public SongList(Context context) {
		super(new SongDAO(context));
		observers = new HashSet<ISongDAOObserver>();
	}

	public DatabaseSongData add(SongData songData) {
		DatabaseSongData databaseSongData = new DatabaseSongData(size() + 1 , dao, songData);
		boolean result = super.add(databaseSongData);
		if (result) {
			notifySongDAOObservers();
		}
		return result ? databaseSongData : null;
	}
	
	public void add(int index, SongData songData) {
		DatabaseSongData databaseSongData = new DatabaseSongData(size() + 1 , dao, songData);
		super.add(index, databaseSongData);
		notifySongDAOObservers();
	}
	
	@Override
	public boolean remove(Object obj) {
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
