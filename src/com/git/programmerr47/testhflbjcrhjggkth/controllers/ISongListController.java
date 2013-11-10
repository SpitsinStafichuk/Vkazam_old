package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import java.util.List;

import android.view.View;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.ISongData;

public interface ISongListController {

	List<ISongData> getList();
	void playPauseSong(ISongData songData);
	boolean deleteSong(ISongData songData);
}
