package com.git.programmerr47.testhflbjcrhjggkth.model.database;


import java.util.HashSet;
import java.util.Set;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongData.SongDataBuilder;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISongDAOObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISongDAOObserver;

import android.content.Context;

@SuppressWarnings("unchecked")
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

	public boolean add(SongDataBuilder builder) {
		boolean result = super.add(builder.setSongDAO(dao).setId(size() + 1).build());
		if (result) {
			notifySongDAOObservers();
		}
		return result;
	}
	
	public void add(int index, SongDataBuilder builder) {
		super.add(index, builder.setSongDAO(dao).setId(size() + 1).build());
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
