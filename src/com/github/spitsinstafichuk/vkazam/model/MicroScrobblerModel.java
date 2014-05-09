package com.github.spitsinstafichuk.vkazam.model;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.github.spitsinstafichuk.vkazam.model.database.Data;
import com.github.spitsinstafichuk.vkazam.model.database.FingerprintList;
import com.github.spitsinstafichuk.vkazam.model.database.SongList;
import com.github.spitsinstafichuk.vkazam.model.managers.FingerprintManager;
import com.github.spitsinstafichuk.vkazam.model.managers.RecognizeListManager;
import com.github.spitsinstafichuk.vkazam.model.managers.RecognizeManager;
import com.github.spitsinstafichuk.vkazam.model.managers.Scrobbler;
import com.github.spitsinstafichuk.vkazam.model.managers.SearchManager;
import com.github.spitsinstafichuk.vkazam.model.managers.SongManager;
import com.github.spitsinstafichuk.vkazam.utils.Constants;
import com.gracenote.mmid.MobileSDK.GNConfig;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.perm.kate.api.Api;

@Deprecated
public class MicroScrobblerModel {
	public static final String RECOGNIZING_SUCCESS = "Success";

	private static MicroScrobblerModel instance;
	private static Context context;
	private final GNConfig config;

	private final FingerprintManager fingerprintManager;
	private final RecognizeManager mainRecognizeManager;
	private final RecognizeListManager recognizeListManager;
	private final SongManager songManager;
	private final SearchManager searchManager;
	private final Handler handler;

	private final SongList songList;
	private final FingerprintList fingerprintList;

	private MicroScrobblerMediaPlayer player;

	private final ImageLoader imageLoader;

	private Api vkApi;
	private final Account vkAccount;

	private final Scrobbler scrobbler;

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
		config = GNConfig.init(Constants.GRACENOTE_APPLICATION_ID, context);
		config.setProperty("content.coverArt", "1");
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
		scrobbler = new Scrobbler(context);
		songManager = new SongManager(handler, context, scrobbler);
		searchManager = new SearchManager(config);
		mainRecognizeManager = new RecognizeManager(config);
		recognizeListManager = new RecognizeListManager(config,
				fingerprintList, songList);
		fingerprintManager = new FingerprintManager(config, context, handler);

		vkAccount = new Account();
		vkAccount.restore(context);

		if (vkAccount.access_token != null) {
			vkApi = new Api(vkAccount.access_token, Constants.VK_API_ID);
		}
		Log.v("vkApi", "vkApi = " + vkApi);
	}

	public RecognizeListManager getRecognizeListManager() {
		return recognizeListManager;
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

	public RecognizeManager getMainRecognizeManager() {
		return mainRecognizeManager;
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

	public MicroScrobblerMediaPlayer getPlayer() {
		player = MicroScrobblerMediaPlayer.getInstance();
		return player;
	}
}
