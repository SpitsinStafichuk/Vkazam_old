package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.LinkedList;

import android.util.Log;

public class FingerprintsDeque<FingerprintData> extends LinkedList<FingerprintData> {

	private OnDequeStateListener listener;
	/**
	 * 
	 */
	private static final long serialVersionUID = -3309742009327045074L;
	
	@Override
	public synchronized void addFirst(FingerprintData data) {
		super.addFirst(data);
        if (size() == 1) {
            Log.v("FingersQueue", "Now queue is not empty");
            if (listener != null) {
                listener.onNonEmpty();
            }
        }
	}
	
	@Override
	public synchronized void addLast(FingerprintData data) {
		super.addLast(data);
        Log.v("Fingers", "deque.size = " + size());
		if (size() == 1) {
            Log.v("FingersQueue", "Now queue is not empty");
            if (listener != null) {
                listener.onNonEmpty();
            }
        }
	}

	public synchronized FingerprintData pollFirst() {
        FingerprintData result = getFirst();
        super.remove(0);
		if (size() > 0) {
            if (listener != null) {
                listener.onNonEmpty();
            }
		} else {
            if (listener != null) {
                listener.onEmpty();
            }
        }
		return result;
	}

    @Override
    public synchronized boolean remove(Object o) {
        boolean result = super.remove(o);
        if (size() == 0) {
            listener.onEmpty();
        }
        return result;
    }

    public void setOnDequeStateListener(OnDequeStateListener listener) {
        this.listener = listener;
    }

    public interface OnDequeStateListener {
        void onNonEmpty();
        void onEmpty();
    }
}
