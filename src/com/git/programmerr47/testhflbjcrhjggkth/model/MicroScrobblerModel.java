package com.git.programmerr47.testhflbjcrhjggkth.model;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.database.*;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.FingerprintManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.RecognizeManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.Scrobbler;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SearchManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.SongManager;
import com.git.programmerr47.testhflbjcrhjggkth.utils.Constants;
import com.gracenote.mmid.MobileSDK.GNConfig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.perm.kate.api.Api;

public class MicroScrobblerModel {
	public static final String RECOGNIZING_SUCCESS = "Success";
	private static final String GRACENOTE_APPLICATION_ID = "5435392-85C21DCCC8BBE15A8B5EE2BDC8A9ACDC";
	
	private static MicroScrobblerModel instance;
	private static Context context;
	private GNConfig config;
	
	private FingerprintManager fingerprintManager;
	private RecognizeManager recognizeManager;
	private SongManager songManager;
	private SearchManager searchManager;
	private Handler handler;
	
	private SongList songList;
	private FingerprintList fingerprintList;
    private FingerprintsDeque fingerprintsDeque;

    private DatabaseSongData currentOpenSong = null;
    private int currentOpenSongPosition = -1;

    private MicroScrobblerMediaPlayer player;
	
	private ImageLoader imageLoader;
	
	private Api vkApi;
	private Account vkAccount;
	
	private Scrobbler scrobbler;
	
	public static void setContext(Context con) {
		context = con;
	}
	
	public static boolean hasContext() {
		return context != null;
	}
	
	@Deprecated
	static synchronized MicroScrobblerModel getInstance() {
		if (instance == null) {
			instance = new MicroScrobblerModel();
		}
		
		return instance;
	}
	
	private MicroScrobblerModel() {
		handler = new Handler();
        MicroScrobblerMediaPlayer.setHandler(handler);
		config = GNConfig.init(GRACENOTE_APPLICATION_ID, context);
		config.setProperty("content.coverArt","1");
		config.setProperty("content.contributor.images", "1");
		config.setProperty("content.contributor.biography", "1");
		config.setProperty("content.artistType", "1");
		config.setProperty("content.artistType.level", "EXTENDED");
		config.setProperty("content.era", "1");
		config.setProperty("content.era.level", "EXTENDED");
		config.setProperty("content.mood", "1");
		config.setProperty("content.mood.level", "EXTENDED");
		config.setProperty("content.origin", "1");
		config.setProperty("content.origin.level", "EXTENDED");
		config.setProperty("content.tempo", "1");
		config.setProperty("content.tempo.level", "EXTENDED");
		config.setProperty("content.genre.level", "EXTENDED");
		config.setProperty("content.review", "1");
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		songList = new SongList(context);
		fingerprintList = new FingerprintList(context);
        fingerprintsDeque = new FingerprintsDeque();
		scrobbler = new Scrobbler(context);
		songManager = new SongManager(handler, context, scrobbler);
		searchManager = new SearchManager(config);
        recognizeManager = new RecognizeManager(config, context);
        fingerprintManager = new FingerprintManager(config, context, handler);
        
        vkAccount = new Account();
		vkAccount.restore(context);

		if (vkAccount.access_token != null) {
			vkApi = new Api(vkAccount.access_token, Constants.VK_API_ID);
		}
		Log.v("vkApi", "vkApi = " + vkApi);
	}
	
	public Scrobbler getScrobbler() {
		return scrobbler;
	}
	
	public FingerprintList getFingerprintList() {
		return fingerprintList;
	}
	
	public SongList getSongList() {
		return songList;
	}
	
	public ImageLoader getImageLoader() {
		return imageLoader;
	}
	
	public SearchManager getSearchManager() {
		return searchManager;
	}
	
	public FingerprintManager getFingerprintManager() {
		return fingerprintManager;
	}
	
	public RecognizeManager getRecognizeManager() {
		return recognizeManager;
	}
	
	public Data getHistoryItem(int position) {
		if ((position > -1) && (position < songList.size())) {
			return songList.get(position);
		} else {
			return null;
		}
	}
	
	public SongManager getSongManager() {
		return songManager;
	}

	public Api getVkApi() {
		return vkApi;
	}
	
	public void setVkApi(String access_token, long user_id, Api vkApi) {
		vkAccount.access_token = access_token;
		vkAccount.user_id = user_id;
		vkAccount.save(context);
		this.vkApi = vkApi;
		Log.v("vkApi", "vkApi = " + vkApi);
	}

    public void setCurrentOpenSong(DatabaseSongData currentOpenSong, int currentOpenSongPosition) {
        this.currentOpenSong = currentOpenSong;
        this.currentOpenSongPosition = currentOpenSongPosition;
    }

    public DatabaseSongData getCurrentOpenSong() {
        return currentOpenSong;
    }

    public int getCurrentOpenSongPosition() {
        return currentOpenSongPosition;
    }

    public MicroScrobblerMediaPlayer getPlayer() {
        player = MicroScrobblerMediaPlayer.getInstance();
        return player;
    }
}
