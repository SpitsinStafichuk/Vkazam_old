package com.github.spitsinstafichuk.vkazam.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.spitsinstafichuk.vkazam.R;
import com.github.spitsinstafichuk.vkazam.adapters_old.VkListAdapter;

public class SongReplaceVkFragment extends FragmentWithName {

	private ListView vkURLs;
	private VkListAdapter adapter;
	private int position;

	public static SongReplaceVkFragment newInstance(int position) {
		SongReplaceVkFragment pageFragment = new SongReplaceVkFragment();
		Bundle arguments = new Bundle();
		arguments.putInt("position", position);
		pageFragment.setArguments(arguments);
		pageFragment.setFragmentName("vk");
		pageFragment.setFragmentIcon(R.drawable.ic_action_vk);
		return pageFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		position = getArguments().getInt("position");
		adapter = new VkListAdapter(this.getActivity(), position,
				R.layout.song_url_list_item, R.layout.more_url_list_item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_history, null);

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
