package com.git.programmerr47.testhflbjcrhjggkth.model.managers;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.FingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.IFingerprintData;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintStatusObservable;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintStatusObserver;
import com.gracenote.mmid.MobileSDK.GNConfig;
import com.gracenote.mmid.MobileSDK.GNFingerprintResult;
import com.gracenote.mmid.MobileSDK.GNFingerprintResultReady;
import com.gracenote.mmid.MobileSDK.GNOperationStatusChanged;
import com.gracenote.mmid.MobileSDK.GNOperations;
import com.gracenote.mmid.MobileSDK.GNSearchResultReady;
import com.gracenote.mmid.MobileSDK.GNStatus;

import android.content.Context;
import android.util.Log;

public class FingerprintManager implements IFingerprintStatusObservable, GNOperationStatusChanged, GNFingerprintResultReady {
	private static final int DEFAULT_FINGERPRINT_TIMER_PERIOD = 10 * 1000;
	public static final String FINGERPRINTING_SUCCESS = "Fingerprinting success";
	
	private GNConfig config;
	private Set<IFingerprintStatusObserver> fingerprintStatusObservers;
	private int fingerprintTimerPeriod;
	private ScheduledThreadPoolExecutor fingerprintTimer;
	private volatile boolean isFingerprinting;
	private boolean isFingerprintingByTimer;
	private boolean isFingerprintingOneTime;
	private String fingerprintStatus;
	
	RecognizeManager recognizeManager;
	
	private IFingerprintData fingerprint;
	
	public FingerprintManager(GNConfig config, Context context, RecognizeManager recognizeManager) {
		fingerprintStatusObservers = new HashSet<IFingerprintStatusObserver>();
		fingerprintTimerPeriod = DEFAULT_FINGERPRINT_TIMER_PERIOD;
		isFingerprinting = false;
		isFingerprintingByTimer = false;
		isFingerprintingOneTime = false;
		this.config = config;
		this.recognizeManager = recognizeManager;
		//возможны проблемы с одновременным обращением к базе данных
	}
	
	public String getFingerprintStatus() {
		return fingerprintStatus;
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
	
	public void setFingerprintStatus(String status) {
		fingerprintStatus = status;
		notifyFingerprintStatusObservers();
	}
	
	@Override
	public void GNStatusChanged(GNStatus status) {
		fingerprintStatus = status.getMessage() + " " + status.getPercentDone() + " %";
		notifyFingerprintStatusObservers();
	}
	
	@Override
	public void GNResultReady(GNFingerprintResult result) {
		isFingerprinting = false;
		isFingerprintingOneTime = false;
		if(result.isFailure()) {
			fingerprintStatus = String.format("[%d] %s", result.getErrCode(),
					result.getErrMessage());
		} else {
			fingerprint = new FingerprintData(-1, result.getFingerprintData(), (new Date()).toString());
			Log.v("Fingerprinting", "fingerprint = " + fingerprint.getFingerprint());
			recognizeManager.recognizeFingerprint(fingerprint, false);
		}
	}
	
	@Override
	public void addObserver(IFingerprintStatusObserver o) {
		fingerprintStatusObservers.add(o);
	}

	@Override
	public void removeObserver(IFingerprintStatusObserver o) {
		fingerprintStatusObservers.remove(o);
	}

	@Override
	public void notifyFingerprintStatusObservers() {
		for(IFingerprintStatusObserver o : fingerprintStatusObservers)
			o.updateFingerprintStatus();
	}
}
