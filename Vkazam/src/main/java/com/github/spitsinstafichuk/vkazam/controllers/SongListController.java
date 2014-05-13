package com.github.spitsinstafichuk.vkazam.controllers;

import java.util.List;

import android.support.v4.app.Fragment;

import com.github.spitsinstafichuk.vkazam.model.database.Data;
import com.github.spitsinstafichuk.vkazam.model.database.DatabaseSongData;

public class SongListController extends SongController {

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
