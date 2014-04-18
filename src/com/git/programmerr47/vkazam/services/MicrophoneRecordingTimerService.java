package com.git.programmerr47.vkazam.services;

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
 * Provides calling MicrophoneRecordingNowService by timer
 *
 * @author Spitsin Michael
 * @since 2014-04-13
 */
public class MicrophoneRecordingTimerService extends MicrophoneRecordingService{

    /**
     * Value that tells about maximum progress of timer delay
     * Because of round progress implementation (as default),
     * progress increments until 360 value will be reached
     */
    private static final int TIMER_PROGRESS_MAX = 360;

    private final Set<OnTimerUpdateListener> onUpdateListeners = new HashSet<OnTimerUpdateListener>();
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    private int timerProgress;


    @Override
    public void recordFingerprint() {
        startServiceWorking();
        if (executor.isShutdown()) {
            executor = new ScheduledThreadPoolExecutor(1);
        }
        timerProgress = 0;
        onUpdate(timerProgress);

        if (isRelativeServiceBound) {
            defRecordingMethod();
        }
    }

    @Override
    public void cancelRecording() {
        if (timerProgress > 0) {
            executor.shutdownNow();
        } else {
            defCancelingMethod();
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
    @SuppressWarnings("unused")
    public int getTimerProgress() {
        return timerProgress;
    }

    /**
     * Clears timer progress
     */
    @SuppressWarnings("unused")
    public void resetProgress() {
        timerProgress = 0;
    }

    @Override
    public void onResultStatus(SongData data) {
        super.onResultStatus(data);
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
                    recordFingerprint();
                }
            }
        }, 0, period, TimeUnit.MILLISECONDS);
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
