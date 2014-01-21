package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;

import com.git.programmerr47.testhflbjcrhjggkth.model.FingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintDAOObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintDAOObserver;

public class FingerprintList extends DatabaseList implements IFingerprintDAOObservable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5708189707888339124L;

	private Set<IFingerprintDAOObserver> observers;

	public FingerprintList(Context context) {
		super(new FingerprintDAO(context));
		observers = new HashSet<IFingerprintDAOObserver>();
	}

	public synchronized DatabaseFingerprintData add(FingerprintData fingerprintData) {
		DatabaseFingerprintData databaseFingerprintData = new DatabaseFingerprintData(size() + 1 , dao, fingerprintData);
		boolean result = super.add(databaseFingerprintData);
		if (result) {
			notifyFigerprintDAOObservers();
		}
		return result ? databaseFingerprintData : null;
	}
	
	public synchronized DatabaseFingerprintData add(int index, FingerprintData fingerprintData) {
		DatabaseFingerprintData databaseFingerprintData = new DatabaseFingerprintData(size() + 1 , dao, fingerprintData);
		super.add(index, databaseFingerprintData);
		notifyFigerprintDAOObservers();
		return databaseFingerprintData;
	}
	
	@Override
	public synchronized boolean remove(Object obj) {
		boolean result = super.remove(obj);
		if (result) {
			notifyFigerprintDAOObservers();
		}
		return result;
	}

	@Override
	public void addObserver(IFingerprintDAOObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(IFingerprintDAOObserver o) {
		observers.remove(o);
	}

	@Override
	public void notifyFigerprintDAOObservers() {
		for(IFingerprintDAOObserver o : observers) 
			o.onFingerprintListChanged();
	}

}
