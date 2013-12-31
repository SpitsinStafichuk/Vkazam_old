package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.git.programmerr47.testhflbjcrhjggkth.R;

public class TimerDelayDialogFragment extends DialogFragment{
    public static final String TAG = "timerDelay";

    public static TimerDelayDialogFragment newInstance() {
        TimerDelayDialogFragment fragment = new TimerDelayDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
        Log.v(TAG, "createdDialog");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_delay, container, false);
        Log.v(TAG, "createdDialogView");
        return view;
    }
}
