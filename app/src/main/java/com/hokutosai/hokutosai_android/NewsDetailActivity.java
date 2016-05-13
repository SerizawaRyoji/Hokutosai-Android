package com.hokutosai.hokutosai_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ryoji on 2016/05/08.
 */
public class NewsDetailActivity extends AppCompatActivity {

    News mNewsDetail;
    ArrayList<News> list;
    MyViewPagerAdapter adapter;

    int mLikeCount;             //いいねの件数

    //Volleyでリクエスト時に設定するタグ名。キャンセル時に利用する。
    private static final Object TAG_NEWS_LIKE_REQUEST_QUEUE = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ニュース");

        setContentView(R.layout.activity_news_detail);

        mNewsDetail = new News();
        list = new ArrayList<>();
        adapter = new MyViewPagerAdapter();

        Intent i = getIntent();
        final News item = (News)i.getSerializableExtra("News");

        mLikeCount = item.getLikes_count();

        TextView title = (TextView)this.findViewById(R.id.news_detail_title);
        TextView date = (TextView)this.findViewById(R.id.news_detail_date);
        title.setText(item.getTitle());
        date.setText( MyDateFormatSingleton.getInstance().getDateTime(item.getDatetime()));

        setLikeClickEvent(item, this);

        String url = "https://api.hokutosai.tech/2016/news/";
        url += String.valueOf(item.getNews_id());
        url += "/details";

        final AppCompatActivity activity = this;

        MyJsonObjectRequest jObjectRequest =
                new MyJsonObjectRequest(Request.Method.GET,url,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                //JSONArrayをListShopItemに変換して取得
                                Gson gson = new Gson();
                                mNewsDetail = gson.fromJson(response.toString(), News.class);

                                //画像の表示******************************************************************************************
                                try {
                                    Gson gsonMedias = new Gson();
                                    JSONArray jArray = response.getJSONArray("medias");    //メニュー取得
                                    //JSONArrayをListShopItemに変換して取得
                                    Type collectionType = new TypeToken<Collection<News.Media>>() {
                                    }.getType();

                                    mNewsDetail.medias = gsonMedias.fromJson(jArray.toString(), collectionType);
                                    if (activity != null) {

                                        final ViewPager viewPager = (ViewPager) activity.findViewById(R.id.news_detail_view_pager);

                                        for (int i = 0; i < mNewsDetail.medias.size(); ++i) {
                                            NetworkImageView view = new NetworkImageView(activity);
                                            view.setImageUrl(mNewsDetail.medias.get(i).url, ImageLoaderSingleton.getImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()));
                                            view.setScaleType(ImageView.ScaleType.FIT_CENTER);

                                            adapter.addView(view);
                                        }

                                        if(  mNewsDetail.medias.size() != 0) {
                                            int w = viewPager.getWidth();
                                            viewPager.getLayoutParams().height = (viewPager.getWidth()*2)/5;    //画像は2:5とする

                                            viewPager.setAdapter(adapter);
                                            CirclePageIndicator circleIndicator = (CirclePageIndicator) activity.findViewById(R.id.news_detail_indicator);
                                            circleIndicator.setViewPager(viewPager);
                                        }
                                    }
                                } catch (JSONException e) {    //menuがnullの時など
                                    // TODO 自動生成された catch ブロック
                                    e.printStackTrace();
                                }
                                //****************************************************************************************************

                                //本文*************************************************************************************************
                                TextView introduction = (TextView)findViewById(R.id.news_detail_text);
                                introduction.setText(mNewsDetail.getText());
                                //*****************************************************************************************************

                                //いいねの表示*******************************************************************************************
                                mLikeCount = mNewsDetail.likes_count;
                                TextView like_count = (TextView)findViewById(R.id.news_detail_like_count);
                                like_count.setText("いいね：" + String.valueOf(mLikeCount) + "件");
                                //*****************************************************************************************************

                                //いいね(クリック可)の表示********************************************************************************
                                ImageView like = (ImageView)findViewById(R.id.news_detail_like_clickable);
                                like.setSelected(mNewsDetail.liked);
                                //*****************************************************************************************************

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

        jObjectRequest.setCustomTimeOut();   //タイムアウト時間の変更
        RequestQueueSingleton.getInstance().add(jObjectRequest);    //WebAPIの呼び出し
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){ // if使うとエラー（itemがInt形式なため）
            case android.R.id.home:   // アプリアイコン（ホームアイコン）を押した時の処理
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        RequestQueueSingleton.getInstance().cancelAll(TAG_NEWS_LIKE_REQUEST_QUEUE);
    }

    private void setLikeClickEvent(final News item, final Activity activity){

        final ImageView like = (ImageView)findViewById(R.id.news_detail_like_clickable);
        // ImageViewオブジェクトにクリックイベントを追加する
        if(like != null) {
            like.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {

                            String url = "https://api.hokutosai.tech/2016/news/" + item.getNews_id() + "/likes";
                            int method = Request.Method.POST;
                            if(item.getLiked()) method = Request.Method.DELETE; //いいね済みの状態でいいねを押した場合はDELETE

                            MyStringRequest postJson = new MyStringRequest(method, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // TODO 自動生成されたメソッド・スタブ
                                            if( activity != null) {
                                                like.setSelected(!like.isSelected());   //画像の反転

                                                TextView like_count = (TextView)findViewById(R.id.news_detail_like_count);

                                                if( like.isSelected() ){
                                                    Toast.makeText(activity, "いいねしました", Toast.LENGTH_SHORT).show();
                                                    ++mLikeCount;
                                                    like_count.setText("いいね：" + String.valueOf(mLikeCount) + "件");
                                                }
                                                else{
                                                    Toast.makeText(activity, "いいねを取り消しました", Toast.LENGTH_SHORT).show();
                                                    --mLikeCount;
                                                    like_count.setText("いいね：" + String.valueOf(mLikeCount) + "件");
                                                }
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            if(activity != null) Toast.makeText(activity, "いいねに失敗しました", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );

                            postJson.setCustomTimeOut();   //タイムアウト時間の変更
                            postJson.setTag(TAG_NEWS_LIKE_REQUEST_QUEUE);    //タグのセット
                            RequestQueueSingleton.getInstance().add(postJson);    //WebAPIの呼び出し
                        }
                    }
            );
        }
    }
}
