package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongDAO;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SearchManager;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.TestPageFragment;

public class TestController {

    private MicroScrobblerModel model;
    private TestPageFragment view;
    private SearchManager searchManager;
    private SongDAO songDAO;

    public TestController(TestPageFragment view) {
            this.view = view;
            model = MicroScrobblerModel.getInstance();
            searchManager = model.getSearchManager();
            songDAO = model.getSongDAO();
    }

    public void search(String artist, String album, String title) {
    	searchManager.search(artist, album, title,  new SearchManager.SearchListener() {
			
			@Override
			public void onSearchStatusChanged(String statusString) {
				view.displayStatus(statusString);
			}
			
			@Override
			public void onSearchResult(SongData songData) {
				songDAO.insert(songData);
				view.displaySongInformationElement(songData);
			}
		});
    }
}

