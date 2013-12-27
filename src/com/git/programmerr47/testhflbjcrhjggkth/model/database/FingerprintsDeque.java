package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.concurrent.LinkedBlockingDeque;

import android.util.Log;
import com.git.programmerr47.testhflbjcrhjggkth.model.FingerprintData;

public class FingerprintsDeque<FingerprintData> extends LinkedBlockingDeque<FingerprintData>{

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
            listener.onNonEmpty();
        }
	}
	
	@Override
	public synchronized void addLast(FingerprintData data) {
		super.addLast(data);
		//if (size() == 1) {
        //    Log.v("FingersQueue", "Now queue is not empty");
        //    listener.onNonEmpty();
        //}
	}
	
	public void setOnDequeStateListener(OnDequeStateListener listener) {
		this.listener = listener;
	}
	
	public interface OnDequeStateListener {
		void onNonEmpty();
	}
}
