package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;

public class FingerprintListController {
	
	MicroScrobblerModel model;
	Activity view;
	
	public FingerprintListController(Fragment fragment) {
		view = fragment.getActivity();
		model = RecognizeServiceConnection.getModel();
	}

}
