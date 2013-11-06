package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.exceptions.LastfmLoginException;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.interfaces.IConnectionWithDialogFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LastfmLoginDialogFragment extends DialogFragment {
    final String LOG_TAG = "myLogs";
    
    EditText login;
    EditText password;
    TextView status;
    ProgressBar lastfmLoginRequestProgress;
    LinearLayout statusLayout;
    Button cancelButton;
    Button loginButton;
   
    private IConnectionWithDialogFragment connectionListener;
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Login form");
        View v = inflater.inflate(R.layout.lastfm_login_dialog, null);
       
        login = (EditText) v.findViewById(R.id.lastfmLogin);
        password =  (EditText) v.findViewById(R.id.lastfmPassword);
        status = (TextView) v.findViewById(R.id.lastfmLoginStatus);
        lastfmLoginRequestProgress = (ProgressBar) v.findViewById(R.id.lastLoginRequestProgress);
        statusLayout = (LinearLayout) v.findViewById(R.id.lastfmLoginStatusLayout);
        cancelButton = (Button) v.findViewById(R.id.lastfmCancelButton);
        loginButton = (Button) v.findViewById(R.id.lastfmLoginButton);
       
        statusLayout.setVisibility(View.GONE);
            status.setVisibility(View.VISIBLE);
       
        loginButton.setOnClickListener(new OnClickListener() {
                   
                    @Override
                    public void onClick(View v) {
                            cancelButton.setEnabled(false);
                            loginButton.setEnabled(false);
                            statusLayout.setVisibility(View.VISIBLE);
                            lastfmLoginRequestProgress.setVisibility(View.VISIBLE);
                            status.setText("Waiting for answer");
                           
                            System.out.println("LastFM Login: " + login.getText().toString());
                            System.out.println("LastFM Password: " + password.getText().toString());
                           
                            final FragmentActivity uiActivity = getActivity();
                            new Thread() {
                                   
                                    @Override
                                    public void run() {
                                           
                                            try {
                                                    MicroScrobblerModel.getInstance().setLastfmAccount(login.getText().toString(), password.getText().toString());

                                                    uiActivity.runOnUiThread(new Runnable() {
                                                           
                                                            @Override
                                                            public void run() {
                                                                    lastfmLoginRequestProgress.setVisibility(View.GONE);
                                                                    status.setText("Success");
                                                            }
                                                    });
                                                   
                                                    try {
                                                            sleep(1000);
                                                    } catch (InterruptedException e) {}
                                                   
                                                    uiActivity.runOnUiThread(new Runnable() {
                                                           
                                                            @Override
                                                            public void run() {
                                                                    cancelButton.setEnabled(true);
                                                                    loginButton.setEnabled(true);
                                                                    dismiss();
                                                            }
                                                    });
                                                    throw new LastfmLoginException();
                                            } catch (final LastfmLoginException e) {
                                                    uiActivity.runOnUiThread(new Runnable() {

                                                            @Override
                                                            public void run() {
                                                                    lastfmLoginRequestProgress.setVisibility(View.GONE);
                                                                    status.setText("Failed :" + e.getMessage());
                                                                    password.setText("");
                                                                    cancelButton.setEnabled(true);
                                                                    loginButton.setEnabled(true);
                                                            }
                                                           
                                                    });
                                            }
                                    }
                                   
                            }.start();
                    }
            });
       
        cancelButton.setOnClickListener(new OnClickListener() {
                   
                    @Override
                    public void onClick(View v) {
                            dismiss();
                    }
            });
       
        return v;
    }
   
    @Override
    public void onAttach(Activity activity) {
            connectionListener = (IConnectionWithDialogFragment) activity;
            super.onAttach(activity);
    }
   
    @Override
    public void onDetach() {
            connectionListener = null;
            super.onDetach();
    }
   
    @Override
    public void onDestroyView() {
            connectionListener.onConnect();
            dismiss();
            super.onDestroyView();
    }
}

