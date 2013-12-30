package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Looper;
import android.os.RemoteException;
import android.widget.Toast;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.musixmatch.lyrics.musiXmatchLyricsConnector;

public class SongInfoController extends SongController{
	
	public SongInfoController(Activity view) {
		super(view);
	}

    public void getLyrics(final SongData data) {
        new Thread(new Runnable() {
            final musiXmatchLyricsConnector lyricsPlugin = new musiXmatchLyricsConnector(view);
            public void run() {
                Looper.prepare();
                long millis = System.currentTimeMillis();
                long s;
                //lyricsPlugin.doBindService();
                do {
                    lyricsPlugin.doBindService();
                    s = System.currentTimeMillis();
                } while (!lyricsPlugin.getIsBound() && !((s - millis) > 1000));
                if (lyricsPlugin.getIsBound()) {
                    final String artist = data.getArtist();
                    final String title = data.getTitle();
                    view.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                lyricsPlugin.startLyricsActivity(artist,title);
                            } catch (RemoteException e) {
                                Toast.makeText(view, "Can not connect to MusiXmatch, try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else { //You need to install Musixmatch\'s app and launch it at least one time to see the lyrics context
                    AlertDialog.Builder appDialogBuilder = new AlertDialog.Builder(view);
                    appDialogBuilder.setIcon(R.drawable.ic_launcher);
                    appDialogBuilder.setMessage("You need to install Musixmatch\\'s app and launch it at least one time to see lyrics");
                    appDialogBuilder.setTitle("No musiXmatch application");
                    appDialogBuilder.setPositiveButton("Get MusiXmatch", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.musixmatch.android.lyrify"));
                            view.startActivity(intent);
                        }
                    });
                    appDialogBuilder.setNegativeButton("No, thanks", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });

                    appDialogBuilder.show();
                }
                Looper.loop();
            }
        }).start();
    }
}
