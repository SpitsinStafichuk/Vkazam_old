package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.SongListController;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.SongListAdapter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class HistoryPageFragment extends Fragment{
	static final String ARGUMENT_RADIO_ID = "arg_rad_id";
    
    int backColor;
    private SongListAdapter adapter;
    private SongListController controller;

    public static HistoryPageFragment newInstance() {
            HistoryPageFragment pageFragment = new HistoryPageFragment();
            Bundle arguments = new Bundle();
            pageFragment.setArguments(arguments);
            return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            
            controller = new SongListController(this);
            adapter = new SongListAdapter(this.getActivity(), R.layout.song_list_item, controller.getList(), controller);
            backColor = Color.argb(255, 255, 255, 255);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.history_fragment, null);
           
            view.setBackgroundColor(backColor);
            
            ListView songHLV = (ListView) view.findViewById(R.id.historyList);
  		  	songHLV.setAdapter(adapter);
            
            return view;
    }
   
    @Override
    public void onDestroy() {
            super.onDestroy();
            Log.v("SongPlayer", "HistoryPageFragment onDestroy()");
    }

}
