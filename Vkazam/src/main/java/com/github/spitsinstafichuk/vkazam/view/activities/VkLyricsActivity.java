package com.github.spitsinstafichuk.vkazam.view.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.spitsinstafichuk.vkazam.R;
import com.github.spitsinstafichuk.vkazam.controllers.SongInfoController;
import com.github.spitsinstafichuk.vkazam.model.MicroScrobblerModel;
import com.github.spitsinstafichuk.vkazam.model.RecognizeServiceConnection;
import com.github.spitsinstafichuk.vkazam.model.SongData;
import com.github.spitsinstafichuk.vkazam.adapters.SongReplacePagerAdapter;
import com.github.spitsinstafichuk.vkazam.view.fragments.MessageDialogFragment;
import com.github.spitsinstafichuk.vkazam.view.fragments.MessageDialogFragment.onDialogClickListener;

public class VkLyricsActivity extends ActionBarActivity {
	public static final String LYRICS_KEY = "vkLyrics";
	public static final String POSITION = "position";
	private static final String SHOW_DIALOG_TAG = "dialog";

	private MicroScrobblerModel model;
	private int position;
	private SongData data;
	private SongInfoController controller;

	private TextView lyrics;
	private String lyricsText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vk_lyrics);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent intent = getIntent();
		lyricsText = intent.getExtras().getString(LYRICS_KEY);
		position = intent.getIntExtra(POSITION, 0);
		Log.i("SongInfoActivity", "VkLyricsActivity.onCreate position: "
				+ position);
		model = RecognizeServiceConnection.getModel();
		data = (SongData) model.getSongList().get(position);
		controller = new SongInfoController(this);

		lyrics = (TextView) findViewById(R.id.vkLyrics);
		lyrics.setText(lyricsText);
	}

	@Override
	public Intent getSupportParentActivityIntent() {
		Intent intent = new Intent(this, SongInfoActivity.class);
		Log.i("SongInfoActivity", "getSupportParentActivityIntent position: "
				+ position);
		intent.putExtra(POSITION, position);
		return intent;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_vk_lyrics_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_bad:
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			Fragment prev = getSupportFragmentManager().findFragmentByTag(
					SHOW_DIALOG_TAG);
			if (prev != null) {
				fragmentTransaction.remove(prev);
			}

			MessageDialogFragment.Builder appDialogBuilder = new MessageDialogFragment.Builder();
			appDialogBuilder.setIcon(R.drawable.ic_alert_dialog);
			appDialogBuilder.setMessage(getString(R.string.bad_lyrics_message));
			appDialogBuilder.setTitle(getString(R.string.bad_lyrics_title));
			appDialogBuilder.setPositiveButton(
					getString(R.string.action_refresh),
					new onDialogClickListener() {

						@Override
						public void onDialogClick(DialogFragment fragment,
								View v) {
							Intent intent = new Intent(VkLyricsActivity.this,
									RefreshPagerActivity.class);
							intent.putExtra(PagerActivity.PAGE_NUMBER,
									SongReplacePagerAdapter.VK_PAGE_NUMBER);
							intent.putExtra("position", position);
							startActivity(intent);
							finish();
							fragment.dismiss();
						}
					});
			appDialogBuilder.setNegativeButton(
					getString(R.string.get_musixmatch_lyrics),
					new onDialogClickListener() {

						@Override
						public void onDialogClick(DialogFragment fragment,
								View v) {
							controller.getMMLyrics(data);
							fragment.dismiss();
						}
					});

			DialogFragment dialogFragment = MessageDialogFragment
					.newInstance(appDialogBuilder);
			dialogFragment.show(fragmentTransaction, SHOW_DIALOG_TAG);
			return true;
		case R.id.settings:
			Log.v("Settings", "Creating settings activity");
			Intent intent = new Intent(VkLyricsActivity.this,
					SettingsActivity.class);
			intent.putExtra(SettingsActivity.PARENT_INTENT, getIntent());
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
