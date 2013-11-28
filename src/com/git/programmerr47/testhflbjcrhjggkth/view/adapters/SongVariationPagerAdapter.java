package com.git.programmerr47.testhflbjcrhjggkth.view.adapters;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.FragmentWithName;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.SongVariationFragment;

public class SongVariationPagerAdapter extends FragmentPagerAdapter{
	
	private static int PAGE_COUNT = 2;
    private List<FragmentWithName> songVariationFragments;
   
    public SongVariationPagerAdapter(FragmentManager fm) {
            super(fm);
            songVariationFragments = new ArrayList<FragmentWithName>();
            songVariationFragments.add(SongVariationFragment.newInstance("Prosto pleer"));
            songVariationFragments.add(SongVariationFragment.newInstance("Vkontakte"));
    }
    
    @Override
    public Fragment getItem(int position) {
    	return songVariationFragments.get(position);
    }
       
     @Override
     public int getCount() {
    	 return PAGE_COUNT;
     }
     
     @Override
     public CharSequence getPageTitle(int position) {
     	return songVariationFragments.get(position).getFragmentName();
     }
}
