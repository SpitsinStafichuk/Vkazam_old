package com.git.programmerr47.testhflbjcrhjggkth.view.adapters;

import java.util.ArrayList;
import java.util.List;

import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.HistoryPageFragment;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.MicrophonePagerFragment;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.RecognizePageFragment;
import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.TestPageFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MicrophonePagerAdapter extends FragmentPagerAdapter {

    static final int PAGE_COUNT = 3;
    List<MicrophonePagerFragment> microphoneFragments;
   
    public MicrophonePagerAdapter(FragmentManager fm) {
            super(fm);
            microphoneFragments = new ArrayList<MicrophonePagerFragment>();
            microphoneFragments.add(RecognizePageFragment.newInstance());
            microphoneFragments.add(HistoryPageFragment.newInstance());
            microphoneFragments.add(TestPageFragment.newInstance());
    }
    
    @Override
    public Fragment getItem(int position) {
    	return microphoneFragments.get(position);
    }
       
     @Override
     public int getCount() {
    	 return PAGE_COUNT;
     }
     
     @Override
     public CharSequence getPageTitle(int position) {
     	return microphoneFragments.get(position).getFragmentName();
     }
}
