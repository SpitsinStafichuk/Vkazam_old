package com.git.programmerr47.testhflbjcrhjggkth.model;

import java.util.List;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.ISongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.LastfmLoginException;
import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.IScrobbler;

public interface IMicroScrobblerModel {

	void setLastfmAccount(String login, String password) throws LastfmLoginException;
	IScrobbler getScrobbler();
	
	List<ISongData> getHistory();
	void deleteLastfmAccount();
}
