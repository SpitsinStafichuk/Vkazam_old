package com.github.spitsinstafichuk.vkazam.controllers;

import java.io.IOException;
import java.net.MalformedURLException;

import com.github.spitsinstafichuk.vkazam.R;

import org.json.JSONException;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.spitsinstafichuk.vkazam.model.MicroScrobblerModel;
import com.github.spitsinstafichuk.vkazam.model.RecognizeServiceConnection;
import com.github.spitsinstafichuk.vkazam.model.database.DatabaseSongData;
import com.github.spitsinstafichuk.vkazam.model.exceptions.SongNotFoundException;
import com.github.spitsinstafichuk.vkazam.model.exceptions.VkAccountNotFoundException;
import com.github.spitsinstafichuk.vkazam.model.managers.SongManager;
import com.github.spitsinstafichuk.vkazam.model.pleer.api.KException;
import com.github.spitsinstafichuk.vkazam.activities.SongInfoActivity;

public class SongController {

    protected MicroScrobblerModel model;

    protected FragmentActivity view;

    private Thread preparingThread;

    public SongController(FragmentActivity view) {
        this.view = view;
        this.model = RecognizeServiceConnection.getModel();
    }

    public synchronized void playPauseSong(final DatabaseSongData songData,
            final int positionInList, final int type) {
        if (preparingThread != null) {
            SongManager songManager = model.getSongManager();
            songManager.set(null, -1, model.getVkApi());
            preparingThread.interrupt();
        }
        preparingThread = new Thread() {
            @Override
            public void run() {
                _playPauseSong(songData, positionInList, type);
                preparingThread = null;
            }
        };
        preparingThread.start();
    }

    public synchronized void playPauseSong(final DatabaseSongData songData,
            final int positionInList) {
        this.playPauseSong(songData, positionInList, SongManager.ANY_SONG);
    }

    public synchronized void playPauseSong(int positionInList) {
        if (positionInList >= model.getSongList().size()) {
            return;
        } else if (positionInList < 0) {
            return;
        }

        if (model.getSongList().size() > 0) {
            this.playPauseSong(((DatabaseSongData) model.getSongList().get(positionInList)),
                    positionInList);
        }
    }

    public void seekTo(int percent) {
        model.getSongManager().seekTo(percent);
    }

    private void _playPauseSong(DatabaseSongData songData, int positionInList, int type) {
        SongManager songManager = model.getSongManager();
        if (songData.equals(songManager.getSongData()) && ((songManager.getType() == type) || (type
                == SongManager.ANY_SONG))) {
            Log.v("SongListController", "songManager.getSongData() == songData == " + songData);
            if (songManager.isPrepared()) {
                if (songManager.isPlaying()) {
                    songManager.pause();
                } else {
                    //TODO �������� ��� ���������� java.lang.IllegalStateException
                    songManager.play();
                }
            }
        } else {
            if (songManager.isPlaying()) {
                songManager.stop();
            }
            if (songManager.isPrepared() || songManager.isLoading()) {
                songManager.release();
            }
            songManager.set(songData, positionInList, model.getVkApi());
            Log.v("SongListController", "song was setted");
            try {
                songManager.prepare(type);
                Log.v("SongListController", "song was prepared ");
                songManager.play();
            } catch (SongNotFoundException e) {
                showToast(view.getString(R.string.song_not_found));
                //songManager.release();
                //songManager.set(null, positionInList, model.getVkApi());
                if (!(view instanceof SongInfoActivity)) {
                    playPauseSong(positionInList + 1);
                } else {
                    songManager.release();
                    songManager.set(null, positionInList, model.getVkApi());
                }
            } catch (MalformedURLException e) {
                showToast(view.getString(R.string.internet_connection_not_available));
                songManager.release();
                songManager.set(null, -1, model.getVkApi());
            } catch (IOException e) {
                showToast(view.getString(R.string.internet_connection_not_available));
                songManager.release();
                songManager.set(null, -1, model.getVkApi());
            } catch (JSONException e) {
                showToast(e.getLocalizedMessage());
                songManager.release();
                songManager.set(null, -1, model.getVkApi());
            } catch (KException e) {
                showToast(e.getLocalizedMessage());
                songManager.release();
                songManager.set(null, -1, model.getVkApi());
            } catch (com.perm.kate.api.KException e) {
                showToast(e.getLocalizedMessage());
                songManager.release();
                songManager.set(null, -1, model.getVkApi());
            } catch (VkAccountNotFoundException e) {
                showToast(view.getString(R.string.vk_not_available));
                songManager.release();
                songManager.set(null, -1, model.getVkApi());
            }
        }
    }

    protected void showToast(final String message) {
        view.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast toast = Toast
                        .makeText(view.getApplicationContext(), message, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
