package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.git.programmerr47.testhflbjcrhjggkth.R;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.git.programmerr47.testhflbjcrhjggkth.model.RecognizeServiceConnection;
import com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api.KException;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.PleerListAdapter;
import org.json.JSONException;

import java.io.IOException;

public class SongReplacePPFragment extends FragmentWithName{

    private ListView ppURLs;
    private BaseAdapter adapter;
	
	public static SongReplacePPFragment newInstance(Context context) {
		SongReplacePPFragment pageFragment = new SongReplacePPFragment();
        Bundle arguments = new Bundle();
        pageFragment.setArguments(arguments);
        pageFragment.setContext(context);
        pageFragment.setFragmentName("pleer");
        pageFragment.setFragmentIcon(R.drawable.ic_action_prostopleer);
        return pageFragment;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new PleerListAdapter(this.getActivity(), R.layout.pp_url_list_item);
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_fragment, null);

        ppURLs = (ListView) view.findViewById(R.id.listView);
        ppURLs.setAdapter(adapter);

        return view;
    }
}
