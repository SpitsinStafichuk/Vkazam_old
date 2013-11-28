package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongList;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SearchManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SearchManager.SearchListener;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.TestPageFragment;

public class TestController {

    private MicroScrobblerModel model;
    private TestPageFragment view;
    private SearchManager searchManager;
    private SongList songList;

    public TestController(TestPageFragment view) {
            this.view = view;
            model = RecognizeServiceConnection.getModel();
            searchManager = model.getSearchManager();
            songList = model.getSongList();
    }

    public void search(String artist, String album, String title) {
    	searchManager.search(artist, album, title,  new SearchManager.SearchListener() {
			
			@Override
			public void onSearchStatusChanged(String statusString) {
				view.displayStatus(statusString);
			}
			
			@Override
			public void onSearchResult(final SongData songData) {
				if(songData != null) {
					final DatabaseSongData databaseSongData = songList.add(songData);
					if(databaseSongData != null) {
						searchManager.search(songData.getTrackId(), new SearchListener() {
							
							@Override
							public void onSearchStatusChanged(String status) {
							}
							
							@Override
							public void onSearchResult(SongData sd) {
								Log.v("TestController", "Songdata = " + sd);
								if (sd != null) {
									Log.v("TestController", "Songdata.coverArt = " + sd.getCoverArtUrl());
									databaseSongData.setNullFields(sd);
								}
								view.displaySongInformationElement(databaseSongData);
							}
						
						});
					}
				}
			}
		});
    }
}

