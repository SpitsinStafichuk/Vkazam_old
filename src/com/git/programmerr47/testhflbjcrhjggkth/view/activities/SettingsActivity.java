package com.git.programmerr47.testhflbjcrhjggkth.view.activities;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.SettingsController;
import com.git.programmerr47.testhflbjcrhjggkth.view.IconCheckBoxPreference;
import com.git.programmerr47.testhflbjcrhjggkth.view.IconPreference;

public class SettingsActivity extends PreferenceActivity {
    SettingsController controller;
    public IconCheckBoxPreference vkConnection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        setContentView(R.layout.settings_layout);

        controller = new SettingsController(this);
        vkConnection = (IconCheckBoxPreference) findPreference("settingsVkConnection");
        changeVkButton();
        vkConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("Settings", "OnVkPreference Click");
                controller.changeVkAccount();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        IconCheckBoxPreference pref;
        IconPreference pref2;
        Resources res = getResources();
        Drawable icon;

        icon = res.getDrawable(R.drawable.ic_settings_vk);
        vkConnection.setIcon(icon);

        pref = (IconCheckBoxPreference) findPreference("settingsVkUrls");
        icon = res.getDrawable(R.drawable.ic_launcher);
        pref.setIcon(icon, View.INVISIBLE);

        pref = (IconCheckBoxPreference) findPreference("settingsOnlyWiFiConntection");
        icon = res.getDrawable(R.drawable.ic_settings_wifi);
        pref.setIcon(icon);

        pref = (IconCheckBoxPreference) findPreference("settingsLastFmConnection");
        icon = res.getDrawable(R.drawable.ic_settings_lastfm);
        pref.setIcon(icon);

        pref = (IconCheckBoxPreference) findPreference("settingsAutoRecognize");
        icon = res.getDrawable(R.drawable.ic_settings_fingerprints);
        pref.setIcon(icon);

        pref2 = (IconPreference) findPreference("settingsTimerDelay");
        icon = res.getDrawable(R.drawable.ic_settings_timer);
        pref2.setIcon(icon);
        pref2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("IconPreference", "!!!Showdialog!!!");
                //show dialog
            }
        });
    }

    public void changeVkButton() {
        vkConnection.setChecked(controller.hasVkAccount());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        controller.checkOnActivityResult(requestCode, resultCode, data);
        vkConnection.setEnabled(true);
    }
}
