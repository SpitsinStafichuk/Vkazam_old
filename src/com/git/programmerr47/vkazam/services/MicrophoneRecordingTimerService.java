package com.git.programmerr47.vkazam.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import com.git.programmerr47.vkazam.model.SongData;
import com.git.programmerr47.vkazam.view.activities.SettingsActivity;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Provides calling MicrophoneRecordingService by timer
 *
 * @author Spitsin Michael
 * @since 2014-04-13
 */
public class MicrophoneRecordingTimerService extends Service implements OnStatusChangedListener{

    public static final int TIMER_PROGRESS_MAX = 360;

    // Binder given to clients
    private final IBinder microphoneRecordingTimerService = new MicrophoneRecordingTimerBinder();
    private final Set<OnTimerUpdateListener> onUpdateListeners = new HashSet<OnTimerUpdateListener>();
    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    private int binderCount;
    private int timerProgress;


    @Override
    public IBinder onBind(Intent intent) {
        binderCount++;
        return microphoneRecordingTimerService;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        binderCount--;
        return super.onUnbind(intent);
    }

    /**
     * Records fingers from microphone by timer
     * Actually: calls record method from MicrophoneRecordingService with fixed delay
     */
    public void record() {
        timerProgress = 0;
        onUpdate(timerProgress);

        //Calls recording
    }

    /**
     * Cancels recording from microphone
     */
    public void cancel() {
        if (timerProgress > 0) {
            executor.shutdownNow();
        } else {
            //Calls cancel
        }
    }

    public void addOnTimerUpdateListener(OnTimerUpdateListener listener) {
        onUpdateListeners.add(listener);
    }

    public void removeOnTimerUpdateListener(OnTimerUpdateListener listener) {
        onUpdateListeners.remove(listener);
    }

    public void onUpdate(int progress) {
        for (OnTimerUpdateListener listener : onUpdateListeners) {
            listener.onUpdate(progress);
        }
    }

    public int getTimerProgress() {
        return timerProgress;
    }

    @Override
    public void onStatusChanged(String status) {
    }

    @Override
    public void onResultStatus(SongData data) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        long period = prefs.getInt("settingsTimerDelay", SettingsActivity.DEFAULT_TIMER_DELAY) * DateUtils.SECOND_IN_MILLIS / TIMER_PROGRESS_MAX;

        timerProgress = 0;
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                timerProgress++;
                onUpdate(timerProgress);
                if (timerProgress >= TIMER_PROGRESS_MAX) {
                    executor.shutdownNow();
                    record();
                }
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class MicrophoneRecordingTimerBinder extends Binder {

        /**
         * @return instance of RecognizeFingerprintService so clients can call public methods
         */
        MicrophoneRecordingTimerService getService() {
            return MicrophoneRecordingTimerService.this;
        }
    }

    /**
     * Provides updating of progress wheel
     * when preparing next recording
     */
    public interface OnTimerUpdateListener {

        /**
         * Calls when progress has updated
         * @param progress - new progress of delay
         */
        void onUpdate(int progress);
    }
}
