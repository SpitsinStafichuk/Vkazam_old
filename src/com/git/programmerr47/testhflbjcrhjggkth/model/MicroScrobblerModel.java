package com.git.programmerr47.testhflbjcrhjggkth.model;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.LastfmLoginException;
import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.IScrobbler;
import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.Scrobbler;

public class MicroScrobblerModel implements IMicroScrobblerModel{
	private static final String SAVE_LASTFM_INFO_PREF = "save lastfm info pref";
	private static final String LASTFM_LOGIN = "lastfm login";
	private static final String LASTFM_PASSWORD = "lastfm password";
	private static final int MODE = Activity.MODE_PRIVATE;
	
	private static IMicroScrobblerModel instance;
	private static Context context;
	
	private IScrobbler scrobbler;
	private SharedPreferences sharedPreferences;
	
	public static void setContext(Context con) {
		context = con;
	}
	
	public static synchronized IMicroScrobblerModel getInstance() {
		if (instance == null) {
			instance = new MicroScrobblerModel();
		}
		
		return instance;
	}
	
	public MicroScrobblerModel() {
		sharedPreferences = context.getSharedPreferences(SAVE_LASTFM_INFO_PREF, MODE);
        final String login = sharedPreferences.getString(LASTFM_LOGIN, null);
        final String password = sharedPreferences.getString(LASTFM_PASSWORD, null);

        new Thread() {
            
            @Override
            public void run() {
                    try {
                            setLastfmAccount(login, password);
                    } catch (LastfmLoginException e) {}
            }
        }.start();
	}
	
	@Override
	public void setLastfmAccount(String login, String password) throws LastfmLoginException {
        scrobbler = new Scrobbler(login, password);
       
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LASTFM_LOGIN, login);
        editor.putString(LASTFM_PASSWORD, password);
        editor.commit();
	}

	@Override
	public IScrobbler getScrobbler() {
		return scrobbler;
	}

	@Override
	public List getHistory() {
		// TODO Auto-generated method stub
		// test
		return null;
	}

}
