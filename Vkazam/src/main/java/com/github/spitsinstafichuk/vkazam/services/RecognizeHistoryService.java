package com.github.spitsinstafichuk.vkazam.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.github.spitsinstafichuk.vkazam.model.SongData;

import java.util.HashSet;
import java.util.Set;

/**
 * Service that manages history of fingers
 * and then send them to RecognizeFingerprintService
 *
 * @author Michael Spitsin
 * @since 2014-04-13
 */
public class RecognizeHistoryService extends Service implements OnStatusChangedListener{

    // Binder given to clients
    private final IBinder recognizeHistoryBinder = new RecognizeHistoryBinder();
    private final Set<OnStatusChangedListener> listeners = new HashSet<OnStatusChangedListener>();

    int binderCount;

    @Override
    public IBinder onBind(Intent intent) {
        binderCount++;
        return recognizeHistoryBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        binderCount--;
        return super.onUnbind(intent);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * Performs sending all Fingers from storage to RecognizeFingerprintService
     * If there is no network, cancels sending
     * Next finger sends only if there is answer from previous fingerprint
     */
    public void startRecognizeFingerprints() {
        //TODO DAO.getLastFingerprint()

    }

    public void addOnStatusChangedListener(OnStatusChangedListener listener) {
        listeners.add(listener);
    }

    public void removeOnStatusChangedListener(OnStatusChangedListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onStatusChanged(String status) {

        if (RecognizeFingerprintService.STATUS_NO_CONNECTION.equals(status)) {
            stopSelf();
        } else {
            for (OnStatusChangedListener listener : listeners) {
                listener.onStatusChanged(status);
            }
        }
    }

    @Override
    public void onResultStatus(SongData data) {
        //TODO delete fingerprint from database

        for (OnStatusChangedListener listener : listeners) {
            listener.onResultStatus(data);
        }

        int fingerCount = 0;//TODO check finger count
        if ((fingerCount == 0) && (binderCount == 0)) {
            stopSelf();
        } else if (fingerCount != 0) {
            startRecognizeFingerprints();
        }
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class RecognizeHistoryBinder extends Binder {

        /**
         * @return instance of RecognizeHistoryService so clients can call public methods
         */
        RecognizeHistoryService getService() {
            return RecognizeHistoryService.this;
        }
    }
}
