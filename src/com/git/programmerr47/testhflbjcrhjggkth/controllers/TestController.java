package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongData.SongDataBuilder;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongList;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SearchManager;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.TestPageFragment;

public class TestController {

    private MicroScrobblerModel model;
    private TestPageFragment view;
    private SearchManager searchManager;
    private SongList songList;

    public TestController(TestPageFragment view) {
            this.view = view;
            model = MicroScrobblerModel.getInstance();
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
			public void onSearchResult(SongDataBuilder builder) {
				songList.add(builder);
				//TODO брать не последний элемент, а тот, который в реальности должен быть
				view.displaySongInformationElement((SongData)songList.get(songList.size() - 1));
			}
		});
    }
}

