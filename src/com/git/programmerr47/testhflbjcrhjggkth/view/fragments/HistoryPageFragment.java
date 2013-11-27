package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.SongListController;
import com.git.programmerr47.testhflbjcrhjggkth.model.MicroScrobblerModel;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongList;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISongDAOObserver;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.SongInfoActivity;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.SongListAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HistoryPageFragment extends MicrophonePagerFragment implements ISongDAOObserver {
	public static final String ARGUMENT_SONG_POSITION = "SongDataPosition";
    
    private SongListAdapter adapter;
    private SongListController controller;
    private Activity parentActivity;
    ListView songHLV;
    SongList songList;

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
            adapter = new SongListAdapter(this.getActivity(), R.layout.list_item, controller);
            songList = RecognizeServiceConnection.getModel().getSongList();
            songList.addObserver(this);
            
            name = "History";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.history_fragment, null);
            
            final Context instance = this.parentActivity;
            songHLV = (ListView) view.findViewById(R.id.historyList);
            songHLV.setAdapter(adapter);
  		  	songHLV.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(instance, SongInfoActivity.class);
					intent.putExtra(ARGUMENT_SONG_POSITION, position);
					startActivity(intent);
				}
			});
  		  	songHLV.setOnScrollListener(new OnScrollListener() {
				int mLastFirstVisibleItem;
  		  		
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					final int currentFirstVisibleItem = view.getFirstVisiblePosition();
					
					if (currentFirstVisibleItem > mLastFirstVisibleItem) {
			            adapter.setScrollingUp(false);
			        } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
			            adapter.setScrollingUp(true);
			        }

			        mLastFirstVisibleItem = currentFirstVisibleItem;
				}
				
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				}
			});
            
            return view;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	adapter.notifyDataSetChanged();
    }
   
    @Override
    public void onDestroy() {
            super.onDestroy();
            songList.removeObserver(this);
            Log.v("SongPlayer", "HistoryPageFragment onDestroy()");
    }
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
    }

	@Override
	public void onHistoryListChanged() {
		adapter.notifyDataSetChanged();
	}

}
