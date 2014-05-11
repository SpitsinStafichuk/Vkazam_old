package com.github.spitsinstafichuk.vkazam.controllers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.github.spitsinstafichuk.vkazam.R;
import com.github.spitsinstafichuk.vkazam.model.MicroScrobblerModel;
import com.github.spitsinstafichuk.vkazam.model.SongData;
import com.github.spitsinstafichuk.vkazam.model.observers.*;
import com.github.spitsinstafichuk.vkazam.activities.MicrophonePagerActivity;

public class NotificationController
        implements ISongInfoObserver, IPlayerStateObserver, IRecognizeStatusObserver,
        IRecognizeResultObserver, IFingerprintStatusObserver {

    private static final int PLAYBACK_SERVICE_STATUS = 1;

    private static final CharSequence PLAYING_STATUS = "Playing song";

    private static final CharSequence LOADING_STATUS = "Loading song";

    private static final CharSequence RECOGNIZING_STATUS = "TAGGING: ";

    private static final CharSequence NOTHING = "Music not found";

    private MicroScrobblerModel model;

    private Service service;

    private Notification notification;

    private NotificationManager notificationManager;

    //private HandlerThread thread;
    //private Handler handler;
    private PendingIntent openIntent;

    private CharSequence status = "MicroScrobbler";

    private CharSequence artistTitle = "Nothing";

    public NotificationController(MicroScrobblerModel model, Service service) {
        //setting variables
        this.model = model;
        this.service = service;
        notificationManager = (NotificationManager) service
                .getSystemService(service.NOTIFICATION_SERVICE);
        //thread = new HandlerThread(NotificationController.class.getName());
        //thread.start();
        //handler = new Handler(thread.getLooper());

        //adding observers
        model.getSongManager().addSongIngoObserver(this);
        model.getFingerprintManager().addFingerprintStatusObserver(this);
        model.getMainRecognizeManager().addRecognizeResultObserver(this);
        model.getMainRecognizeManager().addRecognizeStatusObserver(this);
        model.getPlayer().addPlayerStateObserver(this);

        //init notification
        Intent intent = new Intent(service, MicrophonePagerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openIntent = PendingIntent.getActivity(service, 0, intent, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        recreateNotification();
    }

    public void recreateNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(service);
        notification = builder.
                setContentTitle(status).
                setContentText(artistTitle).
                setContentIntent(openIntent).
                setWhen(0).
                setOngoing(true).
                setSmallIcon(R.drawable.ic_launcher).
                build();
        showNotification();
    }

    public void showNotification() {
        try {
            notificationManager.notify(PLAYBACK_SERVICE_STATUS, notification);
        } catch (Exception e) {
            Log.v("notification", e.getMessage());
        }
    }

    public void hideNotification() {
        if (notificationManager != null) {
            notificationManager.cancel(PLAYBACK_SERVICE_STATUS);
        }
    }

    public void finish() {
        //if (thread != null) {
        //    thread.quit();
        //}

        model.getSongManager().removeSongIngoObserver(this);
        model.getFingerprintManager().removeFingerprintStatusObserver(this);
        model.getMainRecognizeManager().removeRecognizeResultObserver(this);
        model.getMainRecognizeManager().removeRecognizeStatusObserver(this);
        model.getPlayer().removePlayerStateObserver(this);

        hideNotification();
    }

    @Override
    public void updateSongInfo() {
        SongData data = model.getSongManager().getSongData();
        updateSong(PLAYING_STATUS, data);
    }

    @Override
    public void onRecognizeStatusChanged(String status) {
        updateStatus(status);
    }

    @Override
    public void onRecognizeResult(int errorCode, SongData songData) {
        updateSong(RECOGNIZING_STATUS + "Complete", songData);
    }

    @Override
    public void onFingerprintStatusChanged(String status) {
        updateStatus(status);
    }

    private void updateStatus(String status) {
        if (status.contains("Recognizing")) {
            status = status.substring(0, status.indexOf("Recognizing") + 11);
        }
        this.status = RECOGNIZING_STATUS + status;
        String str = artistTitle.toString();
        if (!str.contains("Previous: ")) {
            artistTitle = "Previous: " + artistTitle;
        }
        recreateNotification();
    }

    private void updateSong(CharSequence status, SongData songData) {
        this.status = status;
        if (songData != null) {
            artistTitle = songData.getArtist() + " - " + songData.getTitle();
        } else {
            artistTitle = NOTHING;
        }
        recreateNotification();
    }

    @Override
    public void updatePlayerState() {
        if (model.getPlayer().isLoading()) {
            SongData data = model.getSongManager().getSongData();
            if (data != null) {
                updateSong(LOADING_STATUS, data);
            } else {
                updateStatus(LOADING_STATUS.toString());
            }
        } else if (model.getPlayer().isPlaying()) {
            SongData data = model.getSongManager().getSongData();
            if (data != null) {
                updateSong(PLAYING_STATUS, data);
            }
        }
    }
}
