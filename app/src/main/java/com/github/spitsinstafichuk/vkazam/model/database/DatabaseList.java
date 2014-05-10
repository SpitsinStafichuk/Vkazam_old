package com.github.spitsinstafichuk.vkazam.model.database;

import java.util.Collection;
import java.util.LinkedList;

import android.util.Log;


public abstract class DatabaseList extends LinkedList<Data> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 804357801919315288L;
	protected AbstractDAO dao;
	
	protected DatabaseList(AbstractDAO dao) {
		super();
		this.dao = dao;
		super.addAll(dao.getHistory());
	}
	
	@Override
	public boolean add(Data element) {
		boolean result = super.add(element);
		if (result) {
			result = dao.insert(element) > 0 ? true : false;
			if(!result) {
				super.remove(element);
			}
		}
		return result;
	}
	
	@Override
	public void add(int index, Data element) {
		super.add(index, element);
		boolean result = dao.insert(element) > 0 ? true : false;
		if(!result) {
			super.remove(element);
		}
	}

	@Override
	public boolean addAll(Collection arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int arg0, Collection arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Data remove(int i) {
        Data element = super.remove(i);
        if (element != null) {
            int answer = dao.delete(element);
            Log.v("Deleting", "answer from database is " + answer);
            boolean result = answer == 1 ? true : false;
            if (!result) {
                super.add(i, element);
            }
        }
        return element;
	}

	@Override
	public boolean remove(Object elementObj) {
		Data element = (Data) elementObj;
		int i = indexOf(element);
		boolean result = super.remove(element);
		if (result) {
			result = dao.delete(element) == 1 ? true : false;;
			if(!result) {
				super.add(i, element);
			}
		}
		return result;
	}

	@Override
	public boolean removeAll(Collection arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Data set(int arg0, Data arg1) {
		throw new UnsupportedOperationException();
	}
}
