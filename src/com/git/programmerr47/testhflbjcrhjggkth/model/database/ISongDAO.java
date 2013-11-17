package com.git.programmerr47.testhflbjcrhjggkth.model.database;

import java.util.List;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;


public interface ISongDAO {
	List<SongData> getHistory();
	long insert(SongData songData);
	int delete(SongData songData);
	int update(SongData songData);
}
