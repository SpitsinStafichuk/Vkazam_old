package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.utils.Constants;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.SettingsActivity;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.VkLoginActivity;
import com.perm.kate.api.Api;

public class SettingsController {
	
	private static final String TAG = "SettingsController";
	private static final int REQUEST_VK_LOGIN = 1;

    private MicroScrobblerModel model;
    private SettingsActivity view;

    public SettingsController(SettingsActivity view) {
            this.view = view;
            this.model = RecognizeServiceConnection.getModel();
    }

    public boolean hasVkAccount() {
    	Log.i(TAG, "model.getVkApi() == null: " + (model.getVkApi() == null));
		return model.getVkApi() != null;
	}

	public void changeVkAccount() {
		if (model.getVkApi() == null) {
			view.vkSignInOutButton.setEnabled(false);
			Intent intent = new Intent();
			intent.setClass(view, VkLoginActivity.class);
			view.startActivityForResult(intent, REQUEST_VK_LOGIN);
		} else {
			model.setVkApi(null, 0, null);
			view.changeVkButton();
		}
	}
	
	
	public void checkOnActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_VK_LOGIN) {
			if (resultCode == Activity.RESULT_OK) {
				model.setVkApi(data.getStringExtra("token"), 
						       data.getLongExtra("user_id", 0),
						       new Api(data.getStringExtra("token"), Constants.VK_API_ID));
				view.changeVkButton();
				return;
			}
		}

		model.setVkApi(null, 0, null);
		view.changeVkButton();
	}
}

