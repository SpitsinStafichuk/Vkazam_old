package com.github.spitsinstafichuk.vkazam.activities;

import android.content.Intent;
import android.os.Bundle;

import com.github.spitsinstafichuk.vkazam.adapters.MyPagerAdapter;
import com.github.spitsinstafichuk.vkazam.adapters.SongReplacePagerAdapter;

public class RefreshPagerActivity extends PagerActivity {

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getIntent().getIntExtra("position", 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupUi();
    }

    @Override
    protected MyPagerAdapter getAdapter() {
        return new SongReplacePagerAdapter(position,
                getSupportFragmentManager(), getApplicationContext());
    }

    @Override
    public Intent getSupportParentActivityIntent() {
        Intent intent = new Intent(this, SongInfoActivity.class);
        intent.putExtra("position", position);
        return intent;
    }

}
