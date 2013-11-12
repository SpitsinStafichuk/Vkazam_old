package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.IRecognizeController;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.RecognizeController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.FingerprintManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecognizePageFragment extends Fragment implements IRecognizeStatusObserver {
	static final String ARGUMENT_RADIO_ID = "arg_rad_id";
    
    int backColor;
    IRecognizeController controller;
    MicroScrobblerModel model;
    FingerprintManager fingerprintManager;
    Activity parentActivity;
    
    LinearLayout infoDialog;
    TextView songArtist;
    TextView songTitle;
    TextView songDate;
    TextView status;
    ImageView songCoverArt;

    public static RecognizePageFragment newInstance() {
    		RecognizePageFragment pageFragment = new RecognizePageFragment();
            Bundle arguments = new Bundle();
            pageFragment.setArguments(arguments);
            return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            controller = new RecognizeController(this);
            model = MicroScrobblerModel.getInstance();
            fingerprintManager = model.getFingerprintManager();
            fingerprintManager.addObserver(this);
            backColor = Color.argb(255, 0, 255, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recognize_fragment, null);
        view.setBackgroundColor(backColor);
        
		infoDialog = (LinearLayout) view.findViewById(R.id.apearInformationDialog);
		songArtist = (TextView) view.findViewById(R.id.songInfoArtist);
		songTitle = (TextView) view.findViewById(R.id.songInfoTitle);
		songDate = (TextView) view.findViewById(R.id.songInfoDate);
		status = (TextView) view.findViewById(R.id.status);
		
		songCoverArt = (ImageView) view.findViewById(R.id.songInfoCoverArt);
        
        ImageButton microTimerListenButton = (ImageButton) view.findViewById(R.id.microTimerListenButton);
        microTimerListenButton.setOnLongClickListener(new View.OnLongClickListener(){

			@Override
			public boolean onLongClick(View v) {
				Log.v("Recognizing", "Recognize by timer: onLongClick");
				return controller.recognizeByTimerRecognizeCancel();
			}});
        
        ImageButton microNowListenButton = (ImageButton) view.findViewById(R.id.microNowListenButton);
        microNowListenButton.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Log.v("Recognizing", "Recognize now: onLongClick");
				return controller.recognizeNowRecognizeCancel();
			}
		});
        
        return view;
    }
   
    @Override
    public void onDestroy() {
            super.onDestroy();
            Log.v("SongPlayer", "HistoryPageFragment onDestroy()");
    }

	@Override
	public void updateRecognizeStatus() {
		parentActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				status.setText(fingerprintManager.getRecognizeStatus());
				if(fingerprintManager.getRecognizeStatus().equals(MicroScrobblerModel.RECOGNIZING_SUCCESS)) {
					Bitmap coverArt = fingerprintManager.getCoverArt();
					infoDialog.setVisibility(View.VISIBLE);
					songArtist.setText(fingerprintManager.getArtist());
					songTitle.setText(fingerprintManager.getTitle());
					songDate.setText("just now");
					if (coverArt == null) {
						songCoverArt.setImageResource(R.drawable.no_cover_art);
					} else {
						songCoverArt.setImageBitmap(fingerprintManager.getCoverArt());
					}
				}
			}
		});
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
    }
}
