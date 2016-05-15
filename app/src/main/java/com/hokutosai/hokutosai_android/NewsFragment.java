package com.hokutosai.hokutosai_android;

import android.app.Activity;
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
    ArrayList<News> add_list;
    NewsItemAdapter adapter;
    ListView listView;
    private ProgressBar progress;
    private static final int COUNT = 25;    //ニュースを一度に読み込む数
    private int mLastId = 0;

    Boolean isFirst;
    Boolean mRequestEnded;
    Boolean isStoped;

    static final int REQUEST_NEWS_CODE = 2124;

    //Volleyでリクエスト時に設定するタグ名。キャンセル時に利用する。
    private static final Object TAG_NEWS_REQUEST_QUEUE = new Object();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<>();
        add_list = new ArrayList<>();
        adapter = new NewsItemAdapter( getActivity() );
        listView = null;
        progress = new ProgressBar(getActivity());
        progress.setEnabled(false);

        isFirst = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(list.isEmpty() && getActivity() != null) {
            firstLoadShopList();
        }
        else if(!list.isEmpty() && getActivity() != null){
            //listView = (ListView) getActivity().findViewById(R.id.list_news_view);
            adapter.setNewsItemList(list);
            //listView.setAdapter(adapter);

            //setClickListener();     //クリックしたときの処理について
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        RequestQueueSingleton.getInstance().cancelAll(TAG_NEWS_REQUEST_QUEUE);
        isStoped = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        isStoped = false;
    }

    @Override
    public void onResume() {
        super.onResume();

        if( isFirst ){  //初めてこのメソッドが呼ばれたときは情報が最新なので更新しない
            isFirst = false;
            return ;
        }
        /*int position = listView.getFirstVisiblePosition();
        listView.setSelectionFromTop(position, 0);

        adapter.clearArrayList();   //アダプターのリストをいったん削除
        list.clear();               //リストもいったん削除

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loadShopList(); //サーバーからショップリストを受け取る    アダプター,listに再びセット

                    while( !mRequestEnded && !isStoped){ //web apiが呼び終わるまで待つ *画面遷移した場合は終わらせる
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if( !isStoped )    adapter.notifyDataSetChanged(); //画面の更新
                    }
                });
            }
        }).start();*/
    }

    private void setClickListener(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Intent i = new Intent(getActivity(), NewsDetailActivity.class);
                i.putExtra("News", list.get(position));

                startActivityForResult( i, REQUEST_NEWS_CODE );
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {    //詳細画面から戻ってきたときに呼ばれる
        super.onActivityResult(requestCode, resultCode, data);

        // startActivityForResult()の際に指定した識別コードとの比較
        if( requestCode == REQUEST_NEWS_CODE ){
            // 返却結果ステータスとの比較
            if( resultCode == Activity.RESULT_OK ){

                // 返却されてきたintentから値を取り出す
                News news = (News)data.getSerializableExtra("NewsResult");

                if(getActivity() != null){
                    for(int i=0 ; i<list.size() ; ++i){
                        if(news.getNews_id() == list.get(i).getNews_id()){

                            if( news.getLiked() && !list.get(i).getLiked() || !news.getLiked() && list.get(i).getLiked()){
                                list.get(i).setLiked( news.getLiked() );    //いいねの状態が変わっていたら更新
                                list.get(i).setLikes_count( news.getLikes_count() );

                                adapter.setNewsItemList(list);
                                adapter.notifyDataSetChanged();
                                listView.invalidateViews();
                            }
                            break;
                        }
                    }
                }

            }
        }
    }

    public void setScrollListener(){

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            Boolean isloading = false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

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
                if ((totalItemCount - visibleItemCount) == firstVisibleItem && !isloading) {
                    isloading = true;
                    adapter.setEnabled(false);  //ロード中はクリック不可に

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int now_id = mLastId;
                                // DB読み込みとかitemを作成する時間.
                                addNewsList();

                                while( !mRequestEnded && !isStoped){ //web apiが呼び終わるまで待つ
                                    Thread.sleep(500);
                                }
                                Log.d("test", String.valueOf(mRequestEnded) + " " + "mThreadFrag");

                            } catch (InterruptedException e) {

                            }

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if( isStoped ) return;

                                    // 全て読み込んだらプログレスバーを削除
                                    if (mLastId <= 0) {
                                        listView.removeFooterView(progress);
                                    }
                                    list.addAll(add_list);
                                    adapter.setNewsItemList(list);

                                    adapter.notifyDataSetChanged();
                                    listView.invalidateViews();

                                    isloading = false;
                                    adapter.setEnabled(true);
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }

    public void addNewsList(){

        mRequestEnded = false;

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

                                    add_list = gson.fromJson(response.toString(), collectionType);      //追加分を保持

                                    if(add_list.isEmpty()) mLastId = 0;    //IDを0にしてしまいリストが尽きたことを知らせる
                                    else mLastId = add_list.get(add_list.size()-1).getNews_id(); //取得したニュースのもっとも古いデータを保持

                                    mRequestEnded = true;
                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("LIFE", error.toString());
                                    // エラー処理 error.networkResponseで確認
                                    // エラー表示など
                                    isStoped = true;    //取得できない場合はストップしてしまう
                                }
                            });

            jArrayRequest.setCustomTimeOut();   //タイムアウト時間の変更
            jArrayRequest.setTag(TAG_NEWS_REQUEST_QUEUE);    //タグのセット
            RequestQueueSingleton.getInstance().add(jArrayRequest);    //WebAPIの呼び出し
        }
    }

    public void firstLoadShopList(){

        String url = "https://api.hokutosai.tech/2016/news/timeline";
        String parameter1 = "?count=";
        parameter1 += String.valueOf(COUNT);
        url += parameter1;

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
                                adapter.setNewsItemList(list);

                                if(getActivity() != null) {
                                    //UIに反映
                                    if(listView == null) {
                                        listView = (ListView) getActivity().findViewById(R.id.list_news_view);
                                        listView.addFooterView(progress, null, false);
                                        listView.setAdapter(adapter);
                                        setClickListener();     //クリックしたときの処理について
                                        setScrollListener();    //スクロールしたときの処理について
                                    }
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
}
