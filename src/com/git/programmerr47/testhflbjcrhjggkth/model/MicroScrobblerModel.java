package com.git.programmerr47.testhflbjcrhjggkth.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongDAO;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.data.SongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.IScrobbler;
import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.Scrobbler;
import com.git.programmerr47.testhflbjcrhjggkth.model.lastfm.Scrobbler.IOnSignInResultListener;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.FingerprintManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.RecognizeManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SongInformationManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SongManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISignInObservable;
import com.gracenote.mmid.MobileSDK.GNConfig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MicroScrobblerModel implements ISignInObservable, IOnSignInResultListener {
	private static final String SAVE_LASTFM_INFO_PREF = "save lastfm info pref";
	private static final String LASTFM_USERNAME = "lastfm username";
	private static final String LASTFM_PASSWORD = "lastfm password";
	private static final int MODE = Activity.MODE_PRIVATE;
	public static final String RECOGNIZING_SUCCESS = "Success";
	private static final String GRACENOTE_APPLICATION_ID = "5435392-85C21DCCC8BBE15A8B5EE2BDC8A9ACDC";
	
	private static MicroScrobblerModel instance;
	private static Context context;
	private GNConfig config;
	
	private Scrobbler scrobbler;
	private SharedPreferences sharedPreferences;
	
	private Set<IOnSignInResultListener> listeners;
	
	private FingerprintManager fingerprintManager;
	private RecognizeManager recognizeManager;
	private SongManager songManager;
	private SongInformationManager songInformationManager;
	private Handler handler;
	
	private SongDAO songDAO;
	
	private ImageLoader imageLoader;
	
	public static void setContext(Context con) {
		context = con;
	}
	
	public static boolean hasContext() {
		return context != null;
	}
	
	public static synchronized MicroScrobblerModel getInstance() {
		if (instance == null) {
			instance = new MicroScrobblerModel();
		}
		
		return instance;
	}
	
	private MicroScrobblerModel() {
		handler = new Handler();
		config = GNConfig.init(GRACENOTE_APPLICATION_ID, context);
		config.setProperty("content.coverArt","1");
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		songDAO = new SongDAO(context);
		songManager = new SongManager(songDAO, handler, context);
		songInformationManager = new SongInformationManager(config, songDAO);
		listeners = new HashSet<IOnSignInResultListener>();
		scrobbler = new Scrobbler();
		sharedPreferences = context.getSharedPreferences(SAVE_LASTFM_INFO_PREF, MODE);
        final String login = sharedPreferences.getString(LASTFM_USERNAME, null);
        final String password = sharedPreferences.getString(LASTFM_PASSWORD, null);
        recognizeManager = new RecognizeManager(config, context, scrobbler, songDAO, handler);
        fingerprintManager = new FingerprintManager(config, context, recognizeManager, handler);

        setLastfmAccount(login, password);
	}
	
	public ImageLoader getImageLoader() {
		return imageLoader;
	}
	
	public FingerprintManager getFingerprintManager() {
		return fingerprintManager;
	}
	
	public RecognizeManager getRecognizeManager() {
		return recognizeManager;
	}
	
	public SongInformationManager getSongInformationManager() {
		return songInformationManager;
	}
	
	public List<SongData> getHistory() {
		return songDAO.getHistory();
	}
	
	public void setLastfmAccount(String username, String password) {
        scrobbler.signIn(username, password);
        scrobbler.setOnSignInResultListener(this);
	}
	
	public void deleteLastfmAccount() {
		scrobbler.signOut();
	}

	public IScrobbler getScrobbler() {
		return scrobbler;
	}

    public void setOnSignInResultListener(IOnSignInResultListener listener) {
    	listeners.add(listener);
    }
    
    @Override
    public void removeOnSignInResultListener(IOnSignInResultListener listener) {
    	listeners.remove(listener);
    }
    
    @Override
    public void notifyOnSignInResultListener(String status) {
    	for(IOnSignInResultListener listener : listeners) {
    		listener.onResult(status);
    	}
    }

	@Override
	public void onResult(String status) {
		if(status.equals(STATUS_SUCCESS)) {
			SharedPreferences.Editor editor = sharedPreferences.edit();
	        editor.putString(LASTFM_USERNAME, scrobbler.getCorrectUsername());
	        editor.putString(LASTFM_PASSWORD, scrobbler.getCorrectPassword());
	        editor.commit();
	        songManager.setScrobbler(scrobbler);
		}
		notifyOnSignInResultListener(status);
	}
	
	public SongManager getSongManager() {
		return songManager;
	}
}
