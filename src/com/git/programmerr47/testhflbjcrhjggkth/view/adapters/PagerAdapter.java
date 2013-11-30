package com.git.programmerr47.testhflbjcrhjggkth.view.adapters;

import java.util.ArrayList;
import java.util.List;

import com.git.programmerr47.testhflbjcrhjggkth.view.fragments.FragmentWithName;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

public class PagerAdapter extends FragmentPagerAdapter{

	protected int pageCount = 3;
    protected List<FragmentWithName> fragments;

    public PagerAdapter(FragmentManager fm, int pages) {
        super(fm);
        fragments = new ArrayList<FragmentWithName>();
        pageCount = pages;
    }
    
    public PagerAdapter(FragmentManager fm) {
        this(fm, 3);
    }
    
    @Override
    public Fragment getItem(int position) {
    	return fragments.get(position);
    }
       
     @Override
     public int getCount() {
    	 return pageCount;
     }
     
     @Override
     public CharSequence getPageTitle(int position) {
    	 SpannableStringBuilder sb = new SpannableStringBuilder(" " + fragments.get(position).getFragmentName());
    	 Drawable icon = fragments.get(position).getIcon();
    	 if (icon != null) {
        	 icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        	 ImageSpan span = new ImageSpan(icon, ImageSpan.ALIGN_BOTTOM);
        	 sb.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    	 }
    	 return sb;
     }

}
