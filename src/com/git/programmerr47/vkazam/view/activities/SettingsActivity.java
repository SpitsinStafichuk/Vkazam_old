package com.git.programmerr47.vkazam.view.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.vkazam.controllers.SettingsController;
import com.git.programmerr47.vkazam.model.RecognizeServiceConnection;
import com.git.programmerr47.vkazam.utils.AndroidUtils;
import com.git.programmerr47.vkazam.utils.NetworkUtils;
import com.git.programmerr47.vkazam.view.activities.interfaces.IConnectedDialogFragmentDissmised;
import com.git.programmerr47.vkazam.view.fragments.TimerDelayDialogFragment;
import com.git.programmerr47.vkazam.model.RecognizeServiceConnection;

public class SettingsActivity extends FragmentActivity implements CompoundButton.OnCheckedChangeListener,
                                                                  IConnectedDialogFragmentDissmised {
    private static final String SHOW_DIALOG_TAG = "dialog";

    SettingsController controller;
    public LinearLayout vkConnection;
    private LinearLayout vkUrls;
    private LinearLayout vkAudioBroadcast;
    private LinearLayout vkLyrics;
    private LinearLayout onlyWiFiConntection;
    private LinearLayout lastFmConnection;
    private LinearLayout autoRecognize;
    private LinearLayout timerDelay;
    private CheckBox vkConntectionCheckBox;
    private CheckBox vkUrlsCheckBox;
    private CheckBox vkAudioBroadcastCheckBox;
    private CheckBox vkLyricsCheckBox;
    private CheckBox onlyWiFiConntectionCheckBox;
    private CheckBox lastFmConnectionCheckBox;
    private CheckBox autoRecognizeCheckBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        controller = new SettingsController(this);
        vkConnection = (LinearLayout) findViewById(R.id.settingsVkConnection);
        ((ImageView) vkConnection.findViewById(R.id.icon)).setImageResource(R.drawable.ic_settings_vk);
        ((TextView) vkConnection.findViewById(R.id.title)).setText(getString(R.string.settings_vk_title));
        ((TextView) vkConnection.findViewById(R.id.summary)).setText(getString(R.string.settings_vk_summary));
        vkConntectionCheckBox = (CheckBox) vkConnection.findViewById(R.id.checkbox);
        vkConntectionCheckBox.setOnCheckedChangeListener(this);
        vkConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.changeVkAccount();
            }
        });

        vkUrls = (LinearLayout) findViewById(R.id.settingsVkUrls);
        ((TextView) vkUrls.findViewById(R.id.title)).setText(getString(R.string.settings_vk_urls_title));
        ((TextView) vkUrls.findViewById(R.id.summary)).setText(getString(R.string.settings_vk_urls_summary));
        vkUrlsCheckBox = (CheckBox) vkUrls.findViewById(R.id.checkbox);
        vkUrlsCheckBox.setOnCheckedChangeListener(this);
        vkUrls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vkUrlsCheckBox.setChecked(!vkUrlsCheckBox.isChecked());
            }
        });

        vkAudioBroadcast = (LinearLayout) findViewById(R.id.settingsVkAudioBroadcast);
        ((TextView) vkAudioBroadcast.findViewById(R.id.title)).setText(getString(R.string.settings_vk_status_title));
        ((TextView) vkAudioBroadcast.findViewById(R.id.summary)).setText(getString(R.string.settings_vk_status_summary));
        vkAudioBroadcastCheckBox = (CheckBox) vkAudioBroadcast.findViewById(R.id.checkbox);
        vkAudioBroadcastCheckBox.setOnCheckedChangeListener(this);
        vkAudioBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vkAudioBroadcastCheckBox.setChecked(!vkAudioBroadcastCheckBox.isChecked());
            }
        });

        vkLyrics = (LinearLayout) findViewById(R.id.settingsVkLyrics);
        ((TextView) vkLyrics.findViewById(R.id.title)).setText(getString(R.string.settings_vk_lyrics_title));
        ((TextView) vkLyrics.findViewById(R.id.summary)).setText(getString(R.string.settings_vk_lyrics_summary));
        vkLyricsCheckBox = (CheckBox) vkLyrics.findViewById(R.id.checkbox);
        vkLyricsCheckBox.setOnCheckedChangeListener(this);
        vkLyrics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	vkLyricsCheckBox.setChecked(!vkLyricsCheckBox.isChecked());
            }
        });

        onlyWiFiConntection = (LinearLayout) findViewById(R.id.settingsOnlyWiFiConntection);
        ((ImageView) onlyWiFiConntection.findViewById(R.id.icon)).setImageResource(R.drawable.ic_settings_wifi);
        ((TextView) onlyWiFiConntection.findViewById(R.id.title)).setText(getString(R.string.settings_wifi_title));
        ((TextView) onlyWiFiConntection.findViewById(R.id.summary)).setText(getString(R.string.settings_wifi_summary));
        onlyWiFiConntectionCheckBox = (CheckBox) onlyWiFiConntection.findViewById(R.id.checkbox);
        onlyWiFiConntectionCheckBox.setOnCheckedChangeListener(this);
        onlyWiFiConntection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onlyWiFiConntectionCheckBox.setChecked(!onlyWiFiConntectionCheckBox.isChecked());
            }
        });

        lastFmConnection = (LinearLayout) findViewById(R.id.settingsLastFmConnection);
        ((ImageView) lastFmConnection.findViewById(R.id.icon)).setImageResource(R.drawable.ic_settings_lastfm);
        ((TextView) lastFmConnection.findViewById(R.id.title)).setText(getString(R.string.settings_scrobbling_title));
        ((TextView) lastFmConnection.findViewById(R.id.summary)).setText(getString(R.string.settings_scrobbling_summary));
        lastFmConnectionCheckBox = (CheckBox) lastFmConnection.findViewById(R.id.checkbox);
        lastFmConnectionCheckBox.setOnCheckedChangeListener(this);
        lastFmConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastFmConnectionCheckBox.setChecked(!lastFmConnectionCheckBox.isChecked());
            }
        });

        autoRecognize = (LinearLayout) findViewById(R.id.settingsAutoRecognize);
        ((ImageView) autoRecognize.findViewById(R.id.icon)).setImageResource(R.drawable.ic_settings_fingerprints);
        ((TextView) autoRecognize.findViewById(R.id.title)).setText(getString(R.string.settings_auto_recognizing_title));
        ((TextView) autoRecognize.findViewById(R.id.summary)).setText(getString(R.string.settings_auto_recognizing_summary));
        autoRecognizeCheckBox = (CheckBox) autoRecognize.findViewById(R.id.checkbox);
        autoRecognizeCheckBox.setOnCheckedChangeListener(this);
        autoRecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoRecognizeCheckBox.setChecked(!autoRecognizeCheckBox.isChecked());
                if (NetworkUtils.isNetworkAvailable(SettingsActivity.this)) {
                    if (autoRecognizeCheckBox.isChecked()) {
                        RecognizeServiceConnection.getModel().getRecognizeListManager().recognizeFingerprints();
                    } else {
                        RecognizeServiceConnection.getModel().getRecognizeListManager().cancelAutoRecognize();
                    }
                }
            }
        });

        timerDelay = (LinearLayout) findViewById(R.id.settingsTimerDelay);
        ((ImageView) timerDelay.findViewById(R.id.icon)).setImageResource(R.drawable.ic_settings_timer);
        ((TextView) timerDelay.findViewById(R.id.title)).setText(getString(R.string.settings_timer_delay_title));
        ((TextView) timerDelay.findViewById(R.id.summary)).setText(getString(R.string.settings_timer_delay_summary));
        timerDelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag(SHOW_DIALOG_TAG);
                if (prev != null) {
                    fragmentTransaction.remove(prev);
                }

                DialogFragment timerDelayFragment = TimerDelayDialogFragment.newInstance();
                timerDelayFragment.show(fragmentTransaction, SHOW_DIALOG_TAG);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        resume();
    }
    
    private void resume() {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        ((TextView) timerDelay.findViewById(R.id.additional_info)).setText(prefs.getInt("settingsTimerDelay", 5) + " " + getString(R.string.settings_secs));

        vkConntectionCheckBox.setChecked(prefs.getBoolean("settingsVkConnection", false));
        vkUrlsCheckBox.setChecked(prefs.getBoolean("settingsVkUrls", true));
        vkAudioBroadcastCheckBox.setChecked(prefs.getBoolean("settingsVkAudioBroadcast", true));
        vkLyricsCheckBox.setChecked(prefs.getBoolean("settingsVkLyrics", false));
        onlyWiFiConntectionCheckBox.setChecked(prefs.getBoolean("settingsOnlyWiFiConntection", true));
        lastFmConnectionCheckBox.setChecked(prefs.getBoolean("settingsLastFmConnection", true));
        autoRecognizeCheckBox.setChecked(prefs.getBoolean("settingsAutoRecognize", false));

        AndroidUtils.setViewEnabled(vkUrls, vkConntectionCheckBox.isChecked());
        AndroidUtils.setViewEnabled(vkAudioBroadcast, vkConntectionCheckBox.isChecked());
        AndroidUtils.setViewEnabled(vkLyrics, vkConntectionCheckBox.isChecked());
    }

    public void changeVkButton() {
        vkConntectionCheckBox.setChecked(controller.hasVkAccount());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        controller.checkOnActivityResult(requestCode, resultCode, data);
        vkConnection.setEnabled(true);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=prefs.edit();
        Log.v("Settings", "onCheckedChanged compoundButton = " + compoundButton);
        if (compoundButton == vkConntectionCheckBox) {
            Log.v("Settings", "onCheckedChanged compoundButton = vkConntectionCheckBox");
            editor.putBoolean("settingsVkConnection", b);
            AndroidUtils.setViewEnabled(vkUrls, b);
            AndroidUtils.setViewEnabled(vkAudioBroadcast, b);
            AndroidUtils.setViewEnabled(vkLyrics, b);
        } else if (compoundButton == vkUrlsCheckBox) {
            Log.v("Settings", "onCheckedChanged compoundButton = vkUrlsCheckBox");
            editor.putBoolean("settingsVkUrls", b);
        } else if (compoundButton == vkAudioBroadcastCheckBox) {
            Log.v("Settings", "onCheckedChanged compoundButton = vkAudioBroadcastCheckBox");
            editor.putBoolean("settingsVkAudioBroadcast", b);
        } else if (compoundButton == vkLyricsCheckBox) {
            Log.v("Settings", "onCheckedChanged compoundButton = vkLyricsCheckBox");
            editor.putBoolean("settingsVkLyrics", b);
        } else if (compoundButton == onlyWiFiConntectionCheckBox) {
            Log.v("Settings", "onCheckedChanged compoundButton = onlyWiFiConntectionCheckBox");
            editor.putBoolean("settingsOnlyWiFiConntection", b);
        } else if (compoundButton == lastFmConnectionCheckBox) {
            Log.v("Settings", "onCheckedChanged compoundButton = lastFmConnectionCheckBox");
            editor.putBoolean("settingsLastFmConnection", b);
        } else if (compoundButton == autoRecognizeCheckBox) {
            Log.v("Settings", "onCheckedChanged compoundButton = autoRecognizeCheckBox");
            editor.putBoolean("settingsAutoRecognize", b);
        }
        editor.commit();
    }

    @Override
    public void onComplete() {
        resume();
    }
}
