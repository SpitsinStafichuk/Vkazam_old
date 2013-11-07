package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import com.git.programmerr47.testhflbjcrhjggkth.R;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class RecognizePageFragment extends Fragment {
	static final String ARGUMENT_RADIO_ID = "arg_rad_id";
    
    int backColor;

    public static RecognizePageFragment newInstance() {
    		RecognizePageFragment pageFragment = new RecognizePageFragment();
            Bundle arguments = new Bundle();
            pageFragment.setArguments(arguments);
            return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            backColor = Color.argb(255, 0, 255, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.recognize_fragment, null);
           
            view.setBackgroundColor(backColor);
            
            ImageButton microButton = (ImageButton) view.findViewById(R.id.microListenButton);
            microButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Log.v("Recognize", "Microphone button was pressed");
				}
			});
            
            return view;
    }
   
    @Override
    public void onDestroy() {
            super.onDestroy();
            Log.v("SongPlayer", "HistoryPageFragment onDestroy()");
    }

}
