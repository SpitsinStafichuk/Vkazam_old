package com.git.programmerr47.testhflbjcrhjggkth.view.activities;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Window;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.view.IconCheckBoxPreference;

public class Settings extends PreferenceActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        setContentView(R.layout.settings_layout);
    }

    @Override
    public void onResume() {
        super.onResume();

        IconCheckBoxPreference pref;
        Resources res = getResources();
        Drawable icon;

        pref = (IconCheckBoxPreference) findPreference("settingsVkConnection");
        icon = res.getDrawable(R.drawable.ic_settings_vk);
        pref.setIcon(icon);

        pref = (IconCheckBoxPreference) findPreference("settingsVkUrls");
        icon = res.getDrawable(R.drawable.ic_launcher);
        pref.setIcon(icon);

        pref = (IconCheckBoxPreference) findPreference("settingsOnlyWiFiConntection");
        icon = res.getDrawable(R.drawable.ic_settings_wifi);
        pref.setIcon(icon);

        pref = (IconCheckBoxPreference) findPreference("settingsLastFmConnection");
        icon = res.getDrawable(R.drawable.ic_settings_lastfm);
        pref.setIcon(icon);

        pref = (IconCheckBoxPreference) findPreference("settingsAutoRecognize");
        icon = res.getDrawable(R.drawable.ic_settings_fingerprints);
        pref.setIcon(icon);
    }
}
