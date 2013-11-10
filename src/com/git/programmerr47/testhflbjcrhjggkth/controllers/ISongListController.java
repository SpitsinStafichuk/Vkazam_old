package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import java.util.List;

import android.view.View;

import com.google.sydym6.logic.database.data.ISongData;

public interface ISongListController {

	List<ISongData> getList();
	void playPauseSong(ISongData songData);
	boolean deleteSong(ISongData songData);
}
