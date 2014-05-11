package com.github.spitsinstafichuk.vkazam.controllers;

import android.util.Log;

import com.github.spitsinstafichuk.vkazam.model.MicroScrobblerModel;
import com.github.spitsinstafichuk.vkazam.model.RecognizeServiceConnection;
import com.github.spitsinstafichuk.vkazam.model.SongData;
import com.github.spitsinstafichuk.vkazam.model.database.DatabaseSongData;
import com.github.spitsinstafichuk.vkazam.model.database.SongList;
import com.github.spitsinstafichuk.vkazam.model.managers.SearchManager;
import com.github.spitsinstafichuk.vkazam.model.managers.SearchManager.SearchListener;
import com.github.spitsinstafichuk.vkazam.fragments.TestPageFragment;

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
        searchManager.search(artist, album, title, new SearchManager.SearchListener() {

            @Override
            public void onSearchStatusChanged(String statusString) {
                view.displayStatus(statusString);
            }

            @Override
            public void onSearchResult(final SongData songData) {
                if (songData != null) {
                    final DatabaseSongData databaseSongData = songList.add(0, songData);
                    if (databaseSongData != null) {
                        searchManager.search(songData.getTrackId(), new SearchListener() {

                            @Override
                            public void onSearchStatusChanged(String status) {
                            }

                            @Override
                            public void onSearchResult(SongData sd) {
                                Log.v("TestController", "Songdata = " + sd);
                                if (sd != null) {
                                    Log.v("TestController",
                                            "Songdata.coverArt = " + sd.getCoverArtUrl());
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

