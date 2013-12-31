package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import com.git.programmerr47.testhflbjcrhjggkth.R;

public class TimerDelayDialogFragment extends DialogFragment{
    public static final String TAG = "timerDelay";

    private int result;
    private Button acceptButton;
    private Button cancelButton;
    private EditText resultValue;
    private SeekBar chooseBar;

    public static TimerDelayDialogFragment newInstance() {
        TimerDelayDialogFragment fragment = new TimerDelayDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        result = prefs.getInt("settingsTimerDelay", 5);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_delay, container, false);
        chooseBar = (SeekBar) view.findViewById(R.id.timerDelayChooseBar);
        chooseBar.setProgress(result - 2);
        chooseBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    result = progress + 2;
                    resultValue.setText(result + "");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        resultValue = (EditText) view.findViewById(R.id.timerDelayResultValue);
        resultValue.setText(chooseBar.getProgress() + 2 + "");
        resultValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                try {
                    result = Integer.parseInt(charSequence.toString());
                } catch(Exception e) {
                    result = 5;
                }
                chooseBar.setProgress(result - 2);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimerDelayDialogFragment.this.dismiss();
            }
        });

        acceptButton = (Button) view.findViewById(R.id.acceptButton);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(TimerDelayDialogFragment.this.getActivity());
                SharedPreferences.Editor editor=prefs.edit();
                editor.putInt("settingsTimerDelay", result);
                editor.commit();
                TimerDelayDialogFragment.this.dismiss();
            }
        });

        return view;
    }
}
