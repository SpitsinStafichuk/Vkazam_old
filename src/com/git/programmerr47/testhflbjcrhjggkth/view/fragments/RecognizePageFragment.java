package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.IRecognizeController;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.RecognizeController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.FingerprintManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.managers.RecognizeManager;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintStatusObserver;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import android.app.Activity;
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

public class RecognizePageFragment extends Fragment implements IRecognizeStatusObserver, IFingerprintStatusObserver {
	static final String ARGUMENT_RADIO_ID = "arg_rad_id";
    
    IRecognizeController controller;
    MicroScrobblerModel model;
    RecognizeManager recognizeManager;
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
            recognizeManager = model.getRecognizeManager();
            recognizeManager.addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recognize_fragment, null);
        
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
				return controller.fingerprintByTimerRecognizeCancel();
			}
		});
        
        ImageButton microNowListenButton = (ImageButton) view.findViewById(R.id.microNowListenButton);
        microNowListenButton.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				Log.v("Recognizing", "Recognize now: onLongClick");
				return controller.fingerprintNowRecognizeCancel();
			}
		});
        
        return view;
    }
   
    @Override
    public void onDestroy() {
        super.onDestroy();
        fingerprintManager.removeObserver(this);
        recognizeManager.removeObserver(this);
        Log.v("SongPlayer", "HistoryPageFragment onDestroy()");
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	updateFingerprintStatus();
    	updateRecognizeStatus();
    }

	@Override
	public void updateRecognizeStatus() {
		parentActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (recognizeManager.getRecognizeStatus() != null) {
					status.setText(recognizeManager.getRecognizeStatus());
					if(recognizeManager.getRecognizeStatus().equals(RecognizeManager.RECOGNIZING_SUCCESS)) {
						String coverArtUrl = recognizeManager.getCoverArtUrl();
						infoDialog.setVisibility(View.VISIBLE);
						songArtist.setText(recognizeManager.getArtist());
						songTitle.setText(recognizeManager.getTitle());
						songDate.setText("just now");
						DisplayImageOptions options = new DisplayImageOptions.Builder()
							.showImageForEmptyUri(R.drawable.no_cover_art)
							.showImageOnFail(R.drawable.no_cover_art)
							.build();
						model.getImageLoader().displayImage(coverArtUrl, songCoverArt, options);
					}
				}
			}
		});
	}
	
	@Override
	public void updateFingerprintStatus() {
		parentActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (fingerprintManager.getFingerprintStatus() != null) {
					status.setText(fingerprintManager.getFingerprintStatus());
					if(fingerprintManager.getFingerprintStatus().equals(FingerprintManager.FINGERPRINTING_SUCCESS)) {
						
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
