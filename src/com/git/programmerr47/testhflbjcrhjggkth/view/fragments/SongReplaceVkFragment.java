package com.git.programmerr47.testhflbjcrhjggkth.view.fragments;

import com.git.programmerr47.testhflbjcrhjggkth.R;
import com.git.programmerr47.testhflbjcrhjggkth.view.adapters.VkListAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class SongReplaceVkFragment extends FragmentWithName{
	
	private ListView vkURLs;
	private VkListAdapter adapter;
	
	public static SongReplaceVkFragment newInstance(Context context) {
		SongReplaceVkFragment pageFragment = new SongReplaceVkFragment();
        Bundle arguments = new Bundle();
        pageFragment.setArguments(arguments);
        pageFragment.setContext(context);
        pageFragment.setFragmentName("vk");
        pageFragment.setFragmentIcon(R.drawable.ic_action_vk);
        return pageFragment;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        adapter = new VkListAdapter(this.getActivity(), R.layout.pp_url_list_item, R.layout.more_url_list_item);
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_fragment, null);
        
        vkURLs = (ListView) view.findViewById(R.id.listView);
        vkURLs.setAdapter(adapter);
        
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter.finish();
    }
}
