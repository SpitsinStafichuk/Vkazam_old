package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.FingerprintListController;
import com.git.programmerr47.testhflbjcrhjggkth.controllers.SongListController;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.DatabaseSongData;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.FingerprintList;
import com.git.programmerr47.testhflbjcrhjggkth.model.database.SongList;
import com.git.programmerr47.testhflbjcrhjggkth.model.observers.IFingerprintDAOObserver;
import com.git.programmerr47.testhflbjcrhjggkth.view.activities.SongInfoActivity;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.FingerprintListAdapter;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.SongListAdapter;

public class FingerprintPageFragment extends FragmentWithName implements IFingerprintDAOObserver {
	private FingerprintListAdapter adapter;
    private FingerprintListController controller;
    private ListView fingerprintHLV;
    private FingerprintList fingerprintList;

	public static FingerprintPageFragment newInstance(Context context) {
		FingerprintPageFragment pageFragment = new FingerprintPageFragment();
        Bundle arguments = new Bundle();
        pageFragment.setArguments(arguments);
        pageFragment.setFragmentName("Fingers");
        pageFragment.setFragmentIcon(R.drawable.ic_action_fingerprint);
        pageFragment.setContext(context);
        return pageFragment;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            
            controller = new FingerprintListController(this);
            adapter = new FingerprintListAdapter(this.getActivity(), R.layout.finger_list_item, controller);
            fingerprintList = RecognizeServiceConnection.getModel().getFingerprintList();
            fingerprintList.addObserver(this);
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fingerprints_fragment, null);

        fingerprintHLV = (ListView) view.findViewById(R.id.listView);
        fingerprintHLV.setAdapter(adapter);
        fingerprintHLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.v("Deleting", "Delete fingerprint");
                adapter.deleteFingerprint(view, position);
            }
        });
        fingerprintHLV.setOnScrollListener(new AbsListView.OnScrollListener() {
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
	public void onFingerprintListChanged() {
		adapter.notifyDataSetChanged();
	}
}
