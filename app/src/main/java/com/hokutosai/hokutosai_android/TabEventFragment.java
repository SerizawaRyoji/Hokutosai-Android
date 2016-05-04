package com.hokutosai.hokutosai_android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ryoji on 2016/05/01.
 */
public class TabEventFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Fragment内でFragmentを作成する場合は動的にFragmentを作成しなければならない
        //レイアウトに使うフラグメントを作成
        //TabNewsFragment topicFragment = new TabNewsFragment();
        ScheduleFragment scheduleFragment = new ScheduleFragment();

        //Fragmentを管理するFragmentManagerを取得
        FragmentManager manager = getChildFragmentManager();
        //追加や削除などを1つの処理としてまとめるためのトランザクションクラスを取得
        FragmentTransaction tx = manager.beginTransaction();

        //tx.replace(R.id.layout_fragment_topic_event, topicFragment);
        tx.replace(R.id.layout_fragment_list_event, scheduleFragment);
        tx.commit();

        return inflater.inflate(R.layout.tab_event_fragment, container, false);

    }
}