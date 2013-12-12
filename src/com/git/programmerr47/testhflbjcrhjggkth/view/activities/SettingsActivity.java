package com.git.programmerr47.testhflbjcrhjggkth.view.activities;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.SettingsController;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.interfaces.IConnectionWithDialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingsActivity extends FragmentActivity implements IConnectionWithDialogFragment {
    
    SettingsController controller;
    public Button vkSignInOutButton;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
       
        controller = new SettingsController(this);
       
        //vkSignInOutButton = (Button) findViewById(R.id.vkSingInOutButton);
        changeVkButton();
        vkSignInOutButton.setOnClickListener(new OnClickListener() {
                   
                    @Override
                    public void onClick(View v) {
                            controller.changeVkAccount();
                    }
            });
    }
   
    public void changeVkButton() {
            if (controller.hasVkAccount()) {
                    vkSignInOutButton.setText("Sign Out");
            } else {
                    vkSignInOutButton.setText("Sign In");
            }
    }

    @Override
    public void onConnect() {
            changeVkButton();
            vkSignInOutButton.setEnabled(true);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		controller.checkOnActivityResult(requestCode, resultCode, data);
		vkSignInOutButton.setEnabled(true);
    }
   
}

