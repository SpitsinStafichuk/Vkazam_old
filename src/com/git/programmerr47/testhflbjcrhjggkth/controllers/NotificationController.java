package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.*;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.MicrophonePagerActivity;

public class NotificationController implements ISongInfoObserver, IRecognizeStatusObserver, IRecognizeResultObserver, IFingerprintResultObserver, IFingerprintStatusObserver{

    private static final int PLAYBACK_SERVICE_STATUS = 1;
    private static final CharSequence PLAYING_STATUS = "Playing song";
    private static final CharSequence NOTHING = "Nothing";

    private MicroScrobblerModel model;
    private Service service;
    private Notification notification;
    private NotificationManager notificationManager;
    private HandlerThread thread;
    private Handler handler;
    private PendingIntent openIntent;
    private RemoteViews notificationView;

    private CharSequence status = "No status";
    private CharSequence artistTitle = "Artist - Title";

    public NotificationController(MicroScrobblerModel model, Service service) {
        //setting variables
        this.model = model;
        this.service = service;
        notificationManager = (NotificationManager) service.getSystemService(service.NOTIFICATION_SERVICE);
        thread = new HandlerThread(NotificationController.class.getName());
        thread.start();
        handler = new Handler(thread.getLooper());

        //adding observers
        model.getSongManager().addSongIngoObserver(this);
        model.getFingerprintManager().addFingerprintStatusObserver(this);
        model.getFingerprintManager().addFingerprintResultObserver(this);
        model.getRecognizeManager().addRecognizeResultObserver(this);
        model.getRecognizeManager().addRecognizeStatusObserver(this);

        //init notification
        Intent intent = new Intent(service, MicrophonePagerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        openIntent = PendingIntent.getActivity(service, 0, intent, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        recreateNotification();
    }

    public void recreateNotification() {
        notificationView = new RemoteViews(service.getPackageName(), R.layout.notification_layout);
        notificationView.setTextViewText(R.id.notificationTitle, status);
        notificationView.setTextViewText(R.id.notificationSummary, artistTitle);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(service);
        notification = builder.
                setContent(notificationView).
                setContentIntent(openIntent).
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
        if (thread != null) {
            thread.quit();
        }

        model.getSongManager().removeSongIngoObserver(this);
        model.getFingerprintManager().removeFingerprintStatusObserver(this);
        model.getFingerprintManager().removeFingerprintResultObserver(this);
        model.getRecognizeManager().removeRecognizeResultObserver(this);
        model.getRecognizeManager().removeRecognizeStatusObserver(this);
    }

    @Override
    public void updateSongInfo() {
        SongData data = model.getSongManager().getSongData();
        status = PLAYING_STATUS;
        if (data != null) {
            artistTitle = data.getArtist() + " - " + data.getTitle();
        } else {
            artistTitle = NOTHING;
        }
        recreateNotification();
    }

    @Override
    public void onRecognizeStatusChanged(String status) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onRecognizeResult(SongData songData) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onFingerprintResult(String fingerprint) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onFingerprintStatusChanged(String status) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
