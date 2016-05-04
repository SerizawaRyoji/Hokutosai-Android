package com.hokutosai.hokutosai_android;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ryoji on 2016/05/04.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private List<String> mPagetitles;

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> pagetitles) {
        super(fm);
        this.mFragments = fragments;
        this.mPagetitles = pagetitles;
    }

    @Override
    public Fragment getItem(int position) {
        return this.mFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.mPagetitles.get(position);
    }


    @Override
    public int getCount() {
        return this.mFragments.size();
    }
}
