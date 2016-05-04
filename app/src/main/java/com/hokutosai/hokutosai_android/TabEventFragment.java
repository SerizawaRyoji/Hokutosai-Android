package com.hokutosai.hokutosai_android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryoji on 2016/05/01.
 */
public class TabEventFragment extends Fragment {

    private MyFragmentPagerAdapter mPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO 自動生成されたメソッド・スタブ
        return inflater.inflate(R.layout.tab_event_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Adapterの設定
        List<String> titles = new ArrayList<String>();
        titles.add("全て");
        titles.add("前夜祭");
        //titles.add("1日目");
        //titles.add("2日目");

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new TabShopFragment());
        fragments.add(new TabExhibitionFragment());
        //fragments.add(new TabNewsFragment());
        //fragments.add(new TabNewsFragment());

        mPagerAdapter = new MyFragmentPagerAdapter( getChildFragmentManager(), fragments, titles);
        //ViewPagerを取得
        ViewPager viewPager = (ViewPager)getActivity().findViewById(R.id.events_pager);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip)getActivity().findViewById(R.id.events_tabs);

        tabs.setShouldExpand(true);
        tabs.setIndicatorColor(getResources().getColor(R.color.KosenOrange));
        //adapterの設定
        viewPager.setAdapter(mPagerAdapter);
        //PagerSlidingTabStripにセット
        tabs.setViewPager(viewPager);
    }
}