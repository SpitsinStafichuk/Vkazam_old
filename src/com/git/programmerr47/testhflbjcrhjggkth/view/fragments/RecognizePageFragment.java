package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.RecognizeController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IRecognizeStatusObserver;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class RecognizePageFragment extends Fragment implements IRecognizeStatusObserver {
	static final String ARGUMENT_RADIO_ID = "arg_rad_id";
    
    int backColor;
    RecognizeController controller;

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
            MicroScrobblerModel.getInstance().addObserver(this);
            backColor = Color.argb(255, 0, 255, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.recognize_fragment, null);
            view.setBackgroundColor(backColor);
            ImageButton microListenButton = (ImageButton) view.findViewById(R.id.microListenButton);
            microListenButton.setOnLongClickListener(new View.OnLongClickListener(){

				@Override
				public boolean onLongClick(View v) {
					Log.v("RecognizePageFragment", "onLongClick");
					return controller.recognizeRecognizeCancel();
				}});
            return view;
    }
   
    @Override
    public void onDestroy() {
            super.onDestroy();
            Log.v("SongPlayer", "HistoryPageFragment onDestroy()");
    }

	@Override
	public void updateRecognizeStatus() {
		// TODO Auto-generated method stub
		
	}

}
