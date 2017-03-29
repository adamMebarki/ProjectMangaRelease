package com.mangarelease.adam.projectmangarelease.NewReleaseSource.ContentReleaseSource;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mangarelease.adam.projectmangarelease.ObjectJavaSource.TomeClass;

import java.util.ArrayList;

/**
 * Created by Adam on 12/03/2017.
 */

public class ScreenSlidePagerAdapter extends FragmentPagerAdapter {
    private ArrayList<TomeClass> arraySSPA;

    public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<TomeClass> arrayList) {
        super(fm);
        arraySSPA = arrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return new ScreenSlidePageFragment(arraySSPA.get(position));
    }


    @Override
    public int getCount() {

        return arraySSPA.size();
    }
}