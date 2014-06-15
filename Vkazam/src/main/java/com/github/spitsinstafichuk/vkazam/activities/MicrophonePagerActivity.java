package com.github.spitsinstafichuk.vkazam.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.github.spitsinstafichuk.vkazam.R;
import com.github.spitsinstafichuk.vkazam.adapters.MicrophonePagerAdapter;
import com.github.spitsinstafichuk.vkazam.adapters.MyPagerAdapter;

public class MicrophonePagerActivity extends PagerActivity {

    private static final String TAG = "MicrophonePagerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "Binding to service");
        super.onCreate(savedInstanceState);
        setupUi();
    }

    @Override
    protected MyPagerAdapter getAdapter() {
        return new MicrophonePagerAdapter(getSupportFragmentManager(),
                getApplicationContext());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Log.v("Settings", "Creating settings activity");
                Intent intent = new Intent(this, SettingsActivity.class);
                Intent parentIntent = new Intent(this,
                        MicrophonePagerActivity.class);
                parentIntent.putExtra(PAGE_NUMBER, pager.getCurrentItem());
                intent.putExtra(SettingsActivity.PARENT_INTENT, parentIntent);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
