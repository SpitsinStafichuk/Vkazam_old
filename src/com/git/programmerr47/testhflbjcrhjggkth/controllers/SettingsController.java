package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.support.v4.app.FragmentManager;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.SettingsActivity;

public class SettingsController {

    private MicroScrobblerModel model;
    private SettingsActivity view;

    public SettingsController(SettingsActivity view) {
            this.view = view;
            this.model = RecognizeServiceConnection.getModel();
    }

    public boolean hasLastfmAccount() {
    	return false;
    }

    public void changeLastfmAccount(FragmentManager fm, String tag) {
    }
}

