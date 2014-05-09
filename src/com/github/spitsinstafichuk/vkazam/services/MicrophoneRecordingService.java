package com.github.spitsinstafichuk.vkazam.services;

import android.app.Service;

import com.github.spitsinstafichuk.vkazam.VkazamApplication;
import com.github.spitsinstafichuk.vkazam.model.FingerprintData;
import com.github.spitsinstafichuk.vkazam.model.SongData;
import com.gracenote.mmid.MobileSDK.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Base class for both types of recording from microphone:
 * RecordNow - records from microphone now and just one time
 * RecordTimer - records from microphone with fixed delays
 *
 * @author Michael Spitsin
 * @since 2014-04-18
 */
public abstract class MicrophoneRecordingService extends RelatingService implements GNOperationStatusChanged, GNFingerprintResultReady, OnStatusChangedListener {

    private final Set<OnStatusChangedListener> onStatusListeners = new HashSet<OnStatusChangedListener>();

	/**
	 * Gracenote object to record fingerprints and receive answers
	 */
	protected GNConfig config;

    /**
     * related service that gives messages about current recognizing fingerprint
     */
    private RecognizeFingerprintService recognizeFingerprintService;

    /**
     * Preference of current recognizing fingerprint
     */
    private FingerprintWrapper currentRecognizingWrapper;

    /**
     * Flag that tells is service recording fingerprint from microphone now or not
     */
    private boolean isRecording;

    @Override
    protected void onServiceConnected(Service service) {
        recognizeFingerprintService = (RecognizeFingerprintService) service;
    }

    @Override
    protected Class<?> getRelativeServiceClass() {
        return RecognizeFingerprintService.class;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        config = ((VkazamApplication) getApplication()).getConfig();
    }

	/**
	 * Called when need to record some new fingerprint. Subclasses must override
	 * this method to determine their own mode of recording
	 * 
	 * If subclass does not have its own implementation then it must call
	 * defRecodingMethod()
	 */
	@SuppressWarnings("unused")
	public abstract void recordFingerprint();

	/**
	 * Called when need to cancel some recording of new finger Subclasses must
	 * override this method to determine their own mode of canceling
	 * 
	 * If subclass does not have its own implementation then it must call
	 * defCancelingMethod()
	 */
	@SuppressWarnings("unused")
	public abstract void cancelRecording();

    /**
     * @return priority of fingerprint. Subclasses must tell what sort of priority this fingerprint will be
     */
    protected abstract int getFingerprintPriority();

    /**
     * Default recording functionality
     * Simply starts recording and enables recording flag
     */
    protected void defRecordingMethod() {
        isRecording = true;
        GNOperations.fingerprintMIDStreamFromMic(this, config);
    }

    /**
     * Default cancel recording functionality
     * Simply cancels recording if service do this
     * or cancels recognizing if RecognizeFingerprintService.recognize() is called
     */
    protected void defCancelingMethod() {
        if (isRecording) {
            GNOperations.cancel(this);
            isRecording = false;
        } else if (isWorking()) {
            if (isRelativeServiceBound) {
                recognizeFingerprintService.cancel(currentRecognizingWrapper);
            }
        }
    }

    /**
     * Checks if service is recording
     *
     * @return true if recording, false otherwise
     */
    @SuppressWarnings("unused")
    public boolean isRecording() {
        return isRecording;
    }

    /**
     * @return true if service send fingerprint to RecognizeFingerprintService
     * (system is recognizing fingerprint) ot false otherwise
     */
    @SuppressWarnings("unused")
    public boolean isRecognizing() {
        if (isRelativeServiceBound) {
            return recognizeFingerprintService.isWorking();
        } else {
            return false;
        }
    }

    public void addOnStatusChangedListener(OnStatusChangedListener listener) {
        onStatusListeners.add(listener);
    }

    public void removeOnStatusChangedListener(OnStatusChangedListener listener) {
        onStatusListeners.remove(listener);
    }

    @Override
    public void GNStatusChanged(GNStatus status) {
        onStatusChanged(status.getMessage() + " " + status.getPercentDone() + "%");
    }

    @Override
    public void GNResultReady(GNFingerprintResult gnFingerprintResult) {
        isRecording = false;
        if(gnFingerprintResult.isFailure()) {
            onStatusChanged(String.format("[%d] %s", gnFingerprintResult.getErrCode(), gnFingerprintResult.getErrMessage()));
        } else {
            FingerprintData fingerprint = new FingerprintData(gnFingerprintResult.getFingerprintData(), new Date());
            currentRecognizingWrapper = new FingerprintWrapper(fingerprint, this, FingerprintWrapper.RECOGNIZE_PRIORITY_HIGHEST);
            recognizeFingerprintService.recognize(currentRecognizingWrapper);
        }
    }

    @Override
    public void onStatusChanged(String status) {
        if (RecognizeFingerprintService.STATUS_NO_CONNECTION.equals(status)) {
            //TODO add to db fingerprint
            currentRecognizingWrapper = null;
        } else {
            for (OnStatusChangedListener listener : onStatusListeners) {
                listener.onStatusChanged(status);
            }
        }
    }

    @Override
    public void onResultStatus(SongData data) {
        for (OnStatusChangedListener listener : onStatusListeners) {
            listener.onResultStatus(data);
        }

        currentRecognizingWrapper = null;
    }
}
