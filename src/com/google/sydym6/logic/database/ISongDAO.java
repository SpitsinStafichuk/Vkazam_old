package com.google.sydym6.logic.database;

import java.util.List;

import com.google.sydym6.logic.database.data.ISongData;


public interface ISongDAO {
	List<ISongData> getHistory();
	void insert(ISongData songData);
	int delete(ISongData songData);
}
