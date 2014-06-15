package com.github.spitsinstafichuk.vkazam.activities;

import java.lang.reflect.Field;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.spitsinstafichuk.vkazam.R;
import com.github.spitsinstafichuk.vkazam.view.DepthPageTransformer;
import com.github.spitsinstafichuk.vkazam.view.SmoothPageScroller;
import com.github.spitsinstafichuk.vkazam.adapters.MyPagerAdapter;
import com.github.spitsinstafichuk.vkazam.fragments.FragmentWithName;

public abstract class PagerActivity extends ActionBarActivity {

    public static final String PAGE_NUMBER = "page_number";

    protected ViewPager pager;

    protected MyPagerAdapter pagerAdapter;

    protected int initialPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        Intent intent = getIntent();
        initialPage = intent.getIntExtra(PAGE_NUMBER, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_options_menu, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGE_NUMBER, getSupportActionBar().getSelectedNavigationIndex());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initialPage = savedInstanceState.getInt(PAGE_NUMBER);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Log.v("Settings", "Creating settings activity");
                Intent intent = new Intent(this, SettingsActivity.class);
                Intent parentIntent = getIntent();
                parentIntent.putExtra(PAGE_NUMBER, pager.getCurrentItem());
                intent.putExtra(SettingsActivity.PARENT_INTENT, parentIntent);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    abstract protected MyPagerAdapter getAdapter();

    protected void setupUi() {
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = getAdapter();
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setSelectedNavigationItem(position);
            }
        });
        pager.setPageTransformer(true, new DepthPageTransformer());
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab,
                    FragmentTransaction ft) {
                // hide the given tab
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab,
                    FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            getSupportActionBar()
                    .addTab(getSupportActionBar()
                            .newTab()
                            .setText(
                                    ((FragmentWithName) pagerAdapter.getItem(i))
                                            .getFragmentName())
                            .setTabListener(tabListener));
        }
        getSupportActionBar().setSelectedNavigationItem(initialPage);

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            SmoothPageScroller scroller = new SmoothPageScroller(
                    pager.getContext());
            mScroller.set(pager, scroller);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
