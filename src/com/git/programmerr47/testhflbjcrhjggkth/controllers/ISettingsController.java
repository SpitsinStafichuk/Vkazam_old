package com.git.programmerr47.testhflbjcrhjggkth.controllers;

import android.support.v4.app.FragmentManager;

public interface ISettingsController {

    boolean hasLastfmAccount();
    void changeLastfmAccount(FragmentManager fm, String tag);
}

