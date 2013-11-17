package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.SettingsController;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.interfaces.IConnectionWithDialogFragment;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.LastfmLoginDialogFragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingsActivity extends FragmentActivity implements IConnectionWithDialogFragment {
    
    SettingsController controller;
    public Button lastfmSignInOutButton;
    DialogFragment lastfmDialog;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
       
        controller = new SettingsController(this);
        lastfmDialog = new LastfmLoginDialogFragment();
       
        lastfmSignInOutButton = (Button) findViewById(R.id.lastFmLoginLogoutButton);
        changeLastfmButton();
        lastfmSignInOutButton.setOnClickListener(new OnClickListener() {
                   
                    @Override
                    public void onClick(View v) {
                            controller.changeLastfmAccount(getSupportFragmentManager(), "Login dialog");
                    }
            });
    }
   
    public void changeLastfmButton() {
            if (controller.hasLastfmAccount()) {
                    lastfmSignInOutButton.setText("Sign Out");
            } else {
                    lastfmSignInOutButton.setText("Sign In");
            }
    }

    @Override
    public void onConnect() {
            changeLastfmButton();
            lastfmSignInOutButton.setEnabled(true);
    }
   
}

