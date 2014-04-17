package com.git.programmerr47.vkazam.services;

import android.app.Service;
import android.content.SharedPreferences;
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
public class MicrophoneRecordingTimerService extends RelatingService implements OnStatusChangedListener{

    public static final int TIMER_PROGRESS_MAX = 360;

    private final Set<OnTimerUpdateListener> onUpdateListeners = new HashSet<OnTimerUpdateListener>();

    private MicrophoneRecordingService mService;
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    private int timerProgress;

    /**
     * Records fingers from microphone by timer
     * Actually: calls record method from MicrophoneRecordingService with fixed delay
     */
    public void record() {
        startServiceWorking();
        if (executor.isShutdown()) {
            executor = new ScheduledThreadPoolExecutor(1);
        }
        timerProgress = 0;
        onUpdate(timerProgress);

        if (isRelativeServiceBound) {
            mService.recordFingerprint();
        }
    }

    /**
     * Cancels recording from microphone
     */
    public void cancel() {
        if (timerProgress > 0) {
            executor.shutdownNow();
        } else {
            if (isRelativeServiceBound) {
                mService.cancel();
            }
        }

        stopWorking();
    }

    public void addOnTimerUpdateListener(OnTimerUpdateListener listener) {
        onUpdateListeners.add(listener);
    }

    public void removeOnTimerUpdateListener(OnTimerUpdateListener listener) {
        onUpdateListeners.remove(listener);
    }

    /**
     * Calls when progress has updated
     * @param progress - new progress of delay
     */
    public void onUpdate(int progress) {
        for (OnTimerUpdateListener listener : onUpdateListeners) {
            listener.onUpdate(progress);
        }
    }

    /**
     * @return progress of timer delay between two recordings
     */
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

    @Override
    protected void onServiceConnected(Service service) {
        mService = (MicrophoneRecordingService) service;
        mService.addOnStatusChangedListener(this);
    }

    @Override
    protected Class<?> getRelativeServiceClass() {
        return MicrophoneRecordingService.class;
    }

    @Override
    protected void cleanUpAllDependencies() {
        mService.removeOnStatusChangedListener(this);
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
