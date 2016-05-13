package com.hokutosai.hokutosai_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ryoji on 2016/05/05.
 */
public class NewsFragment extends Fragment {

    ArrayList<News> list;
    NewsItemAdapter adapter;
    ListView listView;
    private ProgressBar progress;
    private static final int COUNT = 25;    //ニュースを一度に読み込む数
    private int mLastId = 0;
    private Boolean mThreadFrag = true;

    //Volleyでリクエスト時に設定するタグ名。キャンセル時に利用する。
    private static final Object TAG_NEWS_REQUEST_QUEUE = new Object();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<>();
        adapter = new NewsItemAdapter( getActivity() );
        listView = null;
        progress = new ProgressBar(getActivity());
        mThreadFrag = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String url = "https://api.hokutosai.tech/2016/news/timeline";
        String parameter1 = "?count=";
		parameter1 += String.valueOf(COUNT);
		url += parameter1;

        if(list.isEmpty() && getActivity() != null) {
            MyJsonArrayRequest jArrayRequest =
                    new MyJsonArrayRequest(url,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    //JSONArrayをListShopItemに変換して取得
                                    Gson gson = new Gson();
                                    Type collectionType = new TypeToken<Collection<News>>() {
                                    }.getType();
                                    list = gson.fromJson(response.toString(), collectionType);

                                    mLastId = list.get(list.size()-1).getNews_id(); //取得したニュースのもっとも古いデータを保持

                                    if(getActivity() != null) {
                                        //UIに反映
                                        listView = (ListView) getActivity().findViewById(R.id.list_news_view);
                                        adapter.setNewsItemList(list);
                                        listView.addFooterView(progress);
                                        listView.setAdapter(adapter);
                                        //Log.d("test", list.get(0).getNews_id() + " " + list.get(0).title);
                                        //Log.d("test", list.get(list.size()-1).getNews_id() + " " + list.get(list.size()-1).title);
                                        setClickListener();     //クリックしたときの処理について
                                        setScrollListener();    //スクロールしたときの処理について
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
            jArrayRequest.setTag(TAG_NEWS_REQUEST_QUEUE);    //タグのセット
            RequestQueueSingleton.getInstance().add(jArrayRequest);    //WebAPIの呼び出し
        }
        else if(!list.isEmpty() && getActivity() != null){
            listView = (ListView) getActivity().findViewById(R.id.list_news_view);
            adapter.setNewsItemList(list);
            listView.setAdapter(adapter);

            setClickListener();     //クリックしたときの処理について
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        RequestQueueSingleton.getInstance().cancelAll(TAG_NEWS_REQUEST_QUEUE);
        mThreadFrag = false;
    }

    private void setClickListener(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Intent i = new Intent(getActivity(), NewsDetailActivity.class);
                i.putExtra("News", list.get(position));
                startActivity(i);
            }
        });
    }

    public void setScrollListener(){

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean isloading = false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, final int firstVisibleItem,final int visibleItemCount,final int totalItemCount) {
                // ロード中の場合は新たにロードしない
                if (isloading) {
                    return;
                }
                // 全て読み込んだら新たにロードしない
                if (mLastId <= 0) {
                    return;
                }

                // (item総数 - 表示されているitem総数) =  表示されているitemのindex
                //  で最下部の検知
                if ((totalItemCount - visibleItemCount) == firstVisibleItem) {
                    isloading = true;
                    adapter.setEnabled(false);  //ロード中はクリック不可に

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int now_id = mLastId;
                                // DB読み込みとかitemを作成する時間.
                                loadNewsList();

                                while( now_id == mLastId && mLastId != 0 && mThreadFrag){ //web apiが呼び終わるまで待つ
                                    Thread.sleep(500);
                                }

                            } catch (InterruptedException e) {

                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d("test","ui thread");

                                    if( !mThreadFrag ) return;

                                    adapter.setNewsItemList(list);
                                    //listView.invalidateViews();
                                    adapter.notifyDataSetChanged();

                                    // 全て読み込んだらプログレスバーを削除
                                    if (mLastId <= 0) {
                                        listView.removeFooterView(progress);
                                    }
                                }
                            });

                            isloading = false;
                            adapter.setEnabled(true);
                        }
                    }).start();
                }
            }
        });
    }

    public void loadNewsList(){

        String url = "https://api.hokutosai.tech/2016/news/timeline";
        String parameter2 = "?last_id=";
        parameter2 += String.valueOf(mLastId - 1);
        url += parameter2;
        url += "&";
        String parameter1 = "count=";
        parameter1 += String.valueOf(COUNT);
        url += parameter1;

        if(!list.isEmpty() && getActivity() != null) {
            MyJsonArrayRequest jArrayRequest =
                    new MyJsonArrayRequest(url,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    //JSONArrayをListShopItemに変換して取得
                                    Gson gson = new Gson();
                                    Type collectionType = new TypeToken<Collection<News>>() {
                                    }.getType();

                                    ArrayList<News> l = gson.fromJson(response.toString(), collectionType);
                                    list.addAll(l);

                                    if(l.isEmpty()) mLastId = 0;    //IDを0にしてしまいリストが尽きたことを知らせる
                                    else mLastId = list.get(list.size()-1).getNews_id(); //取得したニュースのもっとも古いデータを保持
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
            jArrayRequest.setTag(TAG_NEWS_REQUEST_QUEUE);    //タグのセット
            RequestQueueSingleton.getInstance().add(jArrayRequest);    //WebAPIの呼び出し
        }
    }
}
