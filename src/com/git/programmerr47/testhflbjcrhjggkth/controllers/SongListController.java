package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import java.util.List;

import android.support.v4.app.Fragment;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.Data;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;

public class SongListController extends SongController{

	public SongListController(Fragment view) {
		super(view.getActivity());
	}
	
	public List<Data> getList() {
		return model.getSongList();
	}

	public boolean deleteSong(DatabaseSongData songData) {
		//return model.getRadioManager().deleteSongFromHistory(songData);
		return false;
	}
}
