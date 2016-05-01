package com.hokutosai.hokutosai_android;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTabHost mTabHost;

        //メイン画面のレイアウト*********************************************************************
        mTabHost = (FragmentTabHost)findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //タブに使うビューの作成	*第３引数は Fragment で読み込みたいデータ
        View vNews = getLayoutInflater().inflate(R.layout.tab_news, null);
        View vSchedule = getLayoutInflater().inflate(R.layout.tab_schedule, null);
        View vShop = getLayoutInflater().inflate(R.layout.tab_shop, null);
        View vExhibition = getLayoutInflater().inflate(R.layout.tab_exhibition, null);
        //タブの追加
        mTabHost.addTab(mTabHost.newTabSpec("code_id_tab_news").setIndicator(vNews),TabNewsFragment.class, null);				//ニュースタブ
        mTabHost.addTab(mTabHost.newTabSpec("code_id_tab_schedule").setIndicator(vSchedule),TabScheduleFragment.class, null);	//スケジュールタブ
        mTabHost.addTab(mTabHost.newTabSpec("code_id_tab_shop").setIndicator(vShop),TabShopFragment.class, null);				//模擬店タブ
        mTabHost.addTab(mTabHost.newTabSpec("code_id_tab_exhibition").setIndicator(vExhibition),TabShopFragment.class, null);	//展示タブ
    }
}
