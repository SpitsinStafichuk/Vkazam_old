package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.AbsListView;
import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.SongListController;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongList;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.ISongDAOObserver;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.SongInfoActivity;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.SongListAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HistoryPageFragment extends FragmentWithName implements ISongDAOObserver {
	public static final String ARGUMENT_SONGLIST_POSITION = "SongDataPosition";
    
    private SongListAdapter adapter;
    private SongListController controller;
    private Activity parentActivity;
    private ListView songHLV;
    private SongList songList;

    public static HistoryPageFragment newInstance(Context context) {
            HistoryPageFragment pageFragment = new HistoryPageFragment();
            Bundle arguments = new Bundle();
            pageFragment.setArguments(arguments);
            pageFragment.setFragmentName("History");
            pageFragment.setFragmentIcon(R.drawable.ic_action_view_as_list);
            pageFragment.setContext(context);
            return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            
            controller = new SongListController(this);
            adapter = new SongListAdapter(this.getActivity(), R.layout.song_list_item, controller);
            songList = RecognizeServiceConnection.getModel().getSongList();
            songList.addObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.list_view_fragment, null);
            
            final Context instance = this.parentActivity;
            songHLV = (ListView) view.findViewById(R.id.listView);
            songHLV.setAdapter(adapter);
  		  	songHLV.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(instance, SongInfoActivity.class);
                    RecognizeServiceConnection.getModel().setCurrentOpenSong((DatabaseSongData) RecognizeServiceConnection.getModel().getSongList().get(position), position);
                    startActivity(intent);
                }
            });
            songHLV.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    adapter.scrolling();
                }
            });
            
            return view;
    }
    
    @Override
    public void onResume() {
    	super.onResume();

        Fragment miniplayer = new MiniPlayerFragment(adapter);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.miniplayer, miniplayer).commit();

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
