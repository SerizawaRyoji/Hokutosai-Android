package com.hokutosai.hokutosai_android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ryoji on 2016/05/01.
 */
public class TabNewsFragment  extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO 自動生成されたメソッド・スタブ
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Fragment内でFragmentを作成する場合は動的にFragmentを作成しなければならない
        //レイアウトに使うフラグメントを作成
        //EventTopicFragment topicFragment = new EventTopicFragment();
        NewsFragment newsFragment = new NewsFragment();

        //Fragmentを管理するFragmentManagerを取得
        FragmentManager manager = getChildFragmentManager();
        //追加や削除などを1つの処理としてまとめるためのトランザクションクラスを取得
        FragmentTransaction tx = manager.beginTransaction();

        //tx.replace(R.id.layout_fragment_topic_news, topicFragment);
        tx.replace(R.id.layout_fragment_list_news, newsFragment);
        tx.commit();

        return inflater.inflate(R.layout.tab_news_fragment, container, false);
    }

}
