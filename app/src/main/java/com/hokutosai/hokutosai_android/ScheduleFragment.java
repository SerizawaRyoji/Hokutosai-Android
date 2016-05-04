package com.hokutosai.hokutosai_android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.astuetz.PagerSlidingTabStrip;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ryoji on 2016/05/04.
 */
public class ScheduleFragment  extends Fragment {

    private MyFragmentPagerAdapter mPagerAdapter;
    ArrayList<ScheduleItem> list;
    List<String> titles;
    List<Fragment> fragments;

    //Volleyでリクエスト時に設定するタグ名。キャンセル時に利用する。
    private static final Object TAG_EVENT_LIST_REQUEST_QUEUE = new Object();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<>();

        titles = new ArrayList<String>();
        fragments = new ArrayList<Fragment>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO 自動生成されたメソッド・スタブ
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(list.isEmpty()) {
            MyJsonArrayRequest jArrayRequest =
                    new MyJsonArrayRequest("https://api.hokutosai.tech/2016/events/schedules/",
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    //JSONArrayをListShopItemに変換して取得
                                    Gson gson = new Gson();
                                    Type collectionType = new TypeToken<Collection<ScheduleItem>>() {
                                    }.getType();
                                    list = gson.fromJson(response.toString(), collectionType);

                                    if(getActivity() != null) {
                                        //タブのタイトルの作成
                                        titles.add("全て");
                                        titles.add("前夜祭");
                                        titles.add("1日目");
                                        titles.add("2日目");

                                        //タブごとのリストフラグメントを作成
                                        EventListFragment elf01 = new EventListFragment();  //全て
                                        EventListFragment elf02 = new EventListFragment();  //前夜祭
                                        EventListFragment elf03 = new EventListFragment();  //1日目
                                        EventListFragment elf04 = new EventListFragment();  //2日目

                                        List<Event> e = new ArrayList<Event>(); //すべてのイベントリストをまとめる
                                        e.addAll(list.get(0).getTimetable());
                                        e.addAll(list.get(1).getTimetable());
                                        e.addAll(list.get(2).getTimetable());

                                        //それぞれのリストフラグメントにデータを挿入
                                        elf01.setEventListFragment(e);
                                        elf02.setEventListFragment(list.get(0).getTimetable());
                                        elf03.setEventListFragment(list.get(1).getTimetable());
                                        elf04.setEventListFragment(list.get(2).getTimetable());

                                        //フラグメントのリストに追加
                                        fragments.add(elf01);
                                        fragments.add(elf02);
                                        fragments.add(elf03);
                                        fragments.add(elf04);

                                        mPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragments, titles);
                                        //ViewPagerを取得
                                        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.events_pager);
                                        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) getActivity().findViewById(R.id.events_tabs);

                                        tabs.setShouldExpand(true);
                                        tabs.setIndicatorColor(getResources().getColor(R.color.KosenOrange));
                                        //adapterの設定
                                        viewPager.setAdapter(mPagerAdapter);
                                        //PagerSlidingTabStripにセット
                                        tabs.setViewPager(viewPager);
                                    }
                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("LIFE", error.toString());
                                    // エラー処理 error.networkResponseで確認
                                    // エラー表示など
                                }
                            });

            jArrayRequest.setCustomTimeOut();   //タイムアウト時間の変更
            jArrayRequest.setTag(TAG_EVENT_LIST_REQUEST_QUEUE);    //タグのセット
            RequestQueueSingleton.getInstance().add(jArrayRequest);    //WebAPIの呼び出し
        }
        else{
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

    @Override
    public void onStop() {
        super.onStop();
        RequestQueueSingleton.getInstance().cancelAll(TAG_EVENT_LIST_REQUEST_QUEUE);
    }
}