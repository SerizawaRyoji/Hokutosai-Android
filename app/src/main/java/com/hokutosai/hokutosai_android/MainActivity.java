package com.hokutosai.hokutosai_android;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;

    //ナビゲーションドロワーに関わるクラス
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;

    private String[] mDrawerHokutosaiTitles;
    private String[] mDrawerAppTitles;

    private ListView mDrawerHokutosaiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setNavigationDrawer();

        //メイン画面のレイアウト*********************************************************************

        mTabHost = (FragmentTabHost)findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);

        //タブに使うビューの作成	*第３引数は Fragment で読み込みたいデータ
        View vNews = getLayoutInflater().inflate(R.layout.tab_news, null);
        View vSchedule = getLayoutInflater().inflate(R.layout.tab_event, null);
        View vShop = getLayoutInflater().inflate(R.layout.tab_shop, null);
        View vExhibition = getLayoutInflater().inflate(R.layout.tab_exhibition, null);
        //タブの追加
        mTabHost.addTab(mTabHost.newTabSpec("code_id_tab_news").setIndicator(vNews),TabNewsFragment.class, null);				//ニュースタブ
        mTabHost.addTab(mTabHost.newTabSpec("code_id_tab_schedule").setIndicator(vSchedule),TabEventFragment.class, null);	//スケジュールタブ
        mTabHost.addTab(mTabHost.newTabSpec("code_id_tab_shop").setIndicator(vShop),TabShopFragment.class, null);				//模擬店タブ
        mTabHost.addTab(mTabHost.newTabSpec("code_id_tab_exhibition").setIndicator(vExhibition),TabExhibitionFragment.class, null);	//展示タブ
        //****************************************************************************************
    }

    private void setNavigationDrawer(){

        mDrawerHokutosaiTitles = getResources().getStringArray(R.array.drawer_hokutosai_items);

        ArrayAdapter<String> adapterH = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mDrawerHokutosaiTitles);
        mDrawerHokutosaiList = (ListView)this.findViewById(R.id.list_hokutosai_drawer);

        mDrawerHokutosaiList.setAdapter(adapterH);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                //Log.i("LOGTAG", "onDrawerClosed");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //Log.i("LOGTAG", "onDrawerOpened");
            }

	        /*@Override
	        ublic void onDrawerSlide(View drawerView, float slideOffset) {
	            // ActionBarDrawerToggleクラス内の同メソッドにてアイコンのアニメーションの処理をしている。
	            // overrideするときは気を付けること。
	            super.onDrawerSlide(drawerView, slideOffset);
	            Log.i("LOGTAG", "onDrawerSlide : " + slideOffset);
	        }*/

            @Override
            public void onDrawerStateChanged(int newState) {
                // 表示済み、閉じ済みの状態：0
                // ドラッグ中状態:1
                // ドラッグを放した後のアニメーション中：2
                //Log.i("LOGTAG", "onDrawerStateChanged  new state : " + newState);
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawer.addDrawerListener(mDrawerToggle);	//newしたmDrawerToggleをセット

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);	// UpNavigationアイコン(アイコン横の<の部分)を有効に
        // NavigationDrawerではR.drawable.drawerで上書きされている
        // UpNavigationを有効に
        actionbar.setHomeButtonEnabled(true);

        final LinearLayout linear = (LinearLayout)findViewById(R.id.left_drawer);
        // クリック時の挙動を設定
        mDrawerHokutosaiList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        mDrawer.closeDrawer(linear);
                        String url = new String();
                        switch(i){
                            case 0:	//北斗祭公式ホームページ
                                url = "http://www.nc-toyama.ac.jp/c5/index.php/mcon/ca_life/%E3%82%AD%E3%83%A3%E3%83%B3%E3%83%91%E3%82%B9%E3%82%A4%E3%83%99%E3%83%B3%E3%83%88/%E9%AB%98%E5%B0%82%E7%A5%AD/kousensaih008/";
                                break;
                            case 1:	//北斗祭公式Twitter
                                url = "https://mobile.twitter.com/hokutosai2016";
                                break;
                            case 2:	//スクールバス時刻表
                                url = "https://www.hokutosai.tech/schoolbus";
                                break;
                            case 3:	//アプリについて
                                url = "https://www.hokutosai.tech/";
                                break;
                            case 4:	//北斗祭アプリ公式ツイッター
                                url = "https://mobile.twitter.com/hokutosai_app";
                                break;
                            case 5:	//著作権情報
                                url = null;
                                break;
                        }

                        if(url != null){
                            Intent intent = new Intent(MainActivity.this, WebActivity.class);
                            intent.putExtra("URL", url);
                            startActivity(intent);
                        } else{
                            final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            alert.setView(R.layout.dialog_copyright);
                            alert.setTitle("著作権情報");
                            alert.setPositiveButton("OK",null);
                            alert.show();
                        }
                    }
                }
        );
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // ActionBarDrawerToggleにandroid.id.home(up ナビゲーション)を渡す。
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
