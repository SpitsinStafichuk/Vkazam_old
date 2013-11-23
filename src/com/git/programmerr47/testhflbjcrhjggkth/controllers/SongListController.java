package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import java.util.List;

import android.support.v4.app.Fragment;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.Data;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;

public class SongListController extends SongController{

	public SongListController(Fragment view) {
		super(view.getActivity());
	}
	
	public List<Data> getList() {
		return model.getHistory();
	}

	public boolean deleteSong(SongData songData) {
		//return model.getRadioManager().deleteSongFromHistory(songData);
		return false;
	}
}
