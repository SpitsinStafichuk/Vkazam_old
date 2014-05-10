package com.github.spitsinstafichuk.vkazam;

import android.app.Application;

import com.github.spitsinstafichuk.vkazam.utils.Constants;
import com.gracenote.mmid.MobileSDK.GNConfig;

/**
 * Base class for whole app
 *
 * @author Michael Spitsin
 * @since 2014-04-12
 */
public class VkazamApplication extends Application{
    private GNConfig config;

    @Override
    public void onCreate() {
        super.onCreate();

        config = GNConfig.init(Constants.GRACENOTE_APPLICATION_ID, this);
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
    }

    public GNConfig getConfig() {
        return config;
    }
}
