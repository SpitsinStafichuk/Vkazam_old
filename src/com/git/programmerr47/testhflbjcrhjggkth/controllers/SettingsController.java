package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.git.programmerr47.testhflbjcrhjggkth.model.IMicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.LastfmLoginException;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.SettingsActivity;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.LastfmLoginDialogFragment;

public class SettingsController implements ISettingsController {

    private IMicroScrobblerModel model;
    private SettingsActivity view;

    public SettingsController(SettingsActivity view) {
            this.view = view;
            this.model = MicroScrobblerModel.getInstance();
    }

    @Override
    public boolean hasLastfmAccount() {
            return model.getScrobbler().hasLastFmAccount();
    }

    @Override
    public void changeLastfmAccount(FragmentManager fm, String tag) {
            if (hasLastfmAccount()) {
                try {
                    model.setLastfmAccount(null, null);
                } catch (LastfmLoginException e) {
                } finally {
                    view.changeLastfmButton();
                }
            } else {
                view.lastfmSignInOutButton.setEnabled(false);
                DialogFragment lastfmDialog = new LastfmLoginDialogFragment();
                lastfmDialog.show(fm, tag);
            }
    }
}

