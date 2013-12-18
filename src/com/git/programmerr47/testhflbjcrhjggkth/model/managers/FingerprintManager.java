package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.DBConstants;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintResultObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintResultObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintStatusObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintStatusObserver;
import com.gracenote.mmid.MobileSDK.GNConfig;
import com.gracenote.mmid.MobileSDK.GNFingerprintResult;
import com.gracenote.mmid.MobileSDK.GNFingerprintResultReady;
import com.gracenote.mmid.MobileSDK.GNOperationStatusChanged;
import com.gracenote.mmid.MobileSDK.GNOperations;
import com.gracenote.mmid.MobileSDK.GNStatus;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

public class FingerprintManager implements IFingerprintStatusObservable, IFingerprintResultObservable, GNOperationStatusChanged, GNFingerprintResultReady {
	private static final int DEFAULT_FINGERPRINT_TIMER_PERIOD = 20 * 1000;
	public static final String FINGERPRINTING_SUCCESS = "Fingerprinting success";
	public static final String TAG = "FingerprintManager";
	
	private GNConfig config;
	private Handler handler;
	private Set<IFingerprintStatusObserver> fingerprintStatusObservers;
	private Set<IFingerprintResultObserver> fingerprintResultObservers;
	private int fingerprintTimerPeriod;
	private ScheduledThreadPoolExecutor fingerprintTimer;
	private volatile boolean isFingerprinting;
	private boolean isFingerprintingByTimer;
	private boolean isFingerprintingOneTime;
	
	public FingerprintManager(GNConfig config, Context context, Handler handler) {
		fingerprintStatusObservers = new HashSet<IFingerprintStatusObserver>();
		fingerprintResultObservers = new HashSet<IFingerprintResultObserver>();
		fingerprintTimerPeriod = DEFAULT_FINGERPRINT_TIMER_PERIOD;
		isFingerprinting = false;
		isFingerprintingByTimer = false;
		isFingerprintingOneTime = false;
		this.config = config;
		this.handler = handler;
	}
	
	public void fingerprintByTimer() {
		isFingerprintingByTimer = true;
		fingerprintTimer = new ScheduledThreadPoolExecutor(1);
		fingerprintTimer.scheduleWithFixedDelay(new Thread() {

			@Override
			public void run() {
				fingerprint();
			}
			
		}, 0, fingerprintTimerPeriod, TimeUnit.MILLISECONDS);
	}
	
	public void fingerprintByTimerCancel() {
		fingerprintTimer.shutdownNow();
		fingerprintCancel();
		isFingerprintingByTimer = false;
	}
	
	public void fingerprintOneTime() {
		isFingerprintingOneTime = true;
		fingerprint();
	}
	
	public void fingerprintOneTimeCancel() {
		fingerprintCancel();
		isFingerprintingOneTime = false;
	}
	
	private void fingerprint() {
		if (!isFingerprinting) {
			synchronized (this) {
				if (!isFingerprinting) {
					isFingerprinting = true;
					GNOperations.fingerprintMIDStreamFromMic(this, config);
				}
			}
		}
	}
	
	
	private void fingerprintCancel() {
		GNOperations.cancel((GNFingerprintResultReady)this);
		isFingerprinting = false;
	}
	
	public boolean isFingerprintingOneTime() {
		return isFingerprintingOneTime;
	}
	
	public boolean isFingerprintingByTimer() {
		return isFingerprintingByTimer;
	}
	
	@Override
	public void GNStatusChanged(GNStatus status) {
		String fingerprintStatus = status.getMessage() + " " + status.getPercentDone() + " %";
		notifyFingerprintStatusObserversOnUiThread(fingerprintStatus);
	}
	
	@Override
	public void GNResultReady(GNFingerprintResult result) {
		isFingerprinting = false;
		isFingerprintingOneTime = false;
		String fingerprintStatus = null;
		String fingerprint = null;
		if(result.isFailure()) {
			fingerprintStatus = String.format("[%d] %s", result.getErrCode(), result.getErrMessage());
		} else {
			fingerprintStatus = FINGERPRINTING_SUCCESS;
			fingerprint = result.getFingerprintData();
			Log.i(TAG, "fingerprint = " + fingerprint);
		}
		notifyFingerprintStatusObserversOnUiThread(fingerprintStatus);
		notifyFingerprintResultObserversOnUiThread(fingerprint);
	}
	
	@Override
	public void addFingerprintStatusObserver(IFingerprintStatusObserver o) {
		fingerprintStatusObservers.add(o);
	}

	@Override
	public void removeFingerprintStatusObserver(IFingerprintStatusObserver o) {
		fingerprintStatusObservers.remove(o);
	}

	private void notifyFingerprintStatusObserversOnUiThread(final String status) {
		handler.post(new Runnable() {
			public void run() {
				notifyFingerprintStatusObservers(status);
			}
		});
	}
	
	private void notifyFingerprintResultObserversOnUiThread(final String fingerprint) {
		handler.post(new Runnable() {
			public void run() {
				notifyFingerprintResultObservers(fingerprint);
			}
		});
	}

	@Override
	public void addFingerprintResultObserver(IFingerprintResultObserver o) {
		fingerprintResultObservers.add(o);
	}

	@Override
	public void removeFingerprintResultObserver(IFingerprintResultObserver o) {
		fingerprintResultObservers.remove(o);
	}

	@Override
	public void notifyFingerprintResultObservers(String fingerprint) {
		for(IFingerprintResultObserver o : fingerprintResultObservers)
			o.onFingerprintResult(fingerprint);
	}

	@Override
	public void notifyFingerprintStatusObservers(String status) {
		for(IFingerprintStatusObserver o : fingerprintStatusObservers)
			o.onFingerprintStatusChanged(status);
	}
}
