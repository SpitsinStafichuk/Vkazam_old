package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.app.Activity;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.KException;
import com.git.programmerr47.testhflbjcrhjggkth.utils.YoutubeUtils;
import org.json.JSONException;

import java.io.IOException;

public class SongInfoController extends SongController{
	
	public SongInfoController(Activity view) {
		super(view);
	}

    public void showYoutubePage(final SongData data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String youtubeUrl;
                try {
                    youtubeUrl = YoutubeUtils.sendRequest(data.getArtist() + " " + data.getTitle());
                    if (youtubeUrl == null) {
                        Toast.makeText(view, "Can not find video", Toast.LENGTH_SHORT);
                    } else {
                        view.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl)));
                    }
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.  \
                }
            }
        }).start();
    }
}
