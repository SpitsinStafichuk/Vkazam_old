package com.github.spitsinstafichuk.vkazam.view.fragments;

import android.app.Activity;
import android.content.DialogInterface;
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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.github.spitsinstafichuk.vkazam.R;
import com.github.spitsinstafichuk.vkazam.view.activities.SettingsActivity;
import com.github.spitsinstafichuk.vkazam.view.activities.interfaces.IConnectedDialogFragmentDissmised;

public class TimerDelayDialogFragment extends DialogFragment{
    public static final String TAG = "timerDelay";
    public static final int MIN_TIMER_DELAY = 2;
    public static final int MAX_TIMER_DELAY = 300;

    private int result;
    private LinearLayout acceptButton;
    private LinearLayout cancelButton;
    private EditText resultValue;
    private SeekBar chooseBar;

    private IConnectedDialogFragmentDissmised mListener;

    public static TimerDelayDialogFragment newInstance() {
        TimerDelayDialogFragment fragment = new TimerDelayDialogFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (IConnectedDialogFragmentDissmised)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        result = prefs.getInt("settingsTimerDelay", SettingsActivity.DEFAULT_TIMER_DELAY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_delay, container, false);

        TextView min = (TextView) view.findViewById(R.id.minDelay);
        min.setText(MIN_TIMER_DELAY + " " + getString(R.string.settings_secs));

        TextView max = (TextView) view.findViewById(R.id.maxDelay);
        max.setText(MAX_TIMER_DELAY + " " + getString(R.string.settings_secs));

        chooseBar = (SeekBar) view.findViewById(R.id.timerDelayChooseBar);
        chooseBar.setProgress(result - MIN_TIMER_DELAY);
        chooseBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    result = progress + MIN_TIMER_DELAY;
                    resultValue.setText(result + "");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        resultValue = (EditText) view.findViewById(R.id.timerDelayResultValue);
        resultValue.setText(chooseBar.getProgress() + MIN_TIMER_DELAY + "");
        resultValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                try {
                    result = Integer.parseInt(charSequence.toString());
                } catch(Exception e) {
                    result = SettingsActivity.DEFAULT_TIMER_DELAY;
                }
                chooseBar.setProgress(result - MIN_TIMER_DELAY);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        cancelButton = (LinearLayout) view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimerDelayDialogFragment.this.dismiss();
            }
        });

        acceptButton = (LinearLayout) view.findViewById(R.id.acceptButton);
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mListener.onComplete();
    }
}
