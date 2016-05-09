package com.hokutosai.hokutosai_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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

        TextView title = (TextView)this.findViewById(R.id.news_detail_title);
        title.setText(item.getTitle());

        String url = "https://api.hokutosai.tech/2016/news/";
        url += String.valueOf(item.getNews_id());
        url += "/details";

        final AppCompatActivity activity = this;

        MyJsonObjectRequest jObjectRequest =
                new MyJsonObjectRequest(Request.Method.GET,url,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("test", "newsだよ" + response.toString());
                                //JSONArrayをListShopItemに変換して取得
                                Gson gson = new Gson();
                                mNewsDetail = gson.fromJson(response.toString(), News.class);


                                //関連の表示*********************************************************************************************
                                TextView place = (TextView)findViewById(R.id.news_detail_related);
                                place.setText("related_text");
                                //*****************************************************************************************************

                                //いいねの表示*******************************************************************************************
                                ImageView like = (ImageView)findViewById(R.id.news_detail_like);
                                TextView like_count = (TextView)findViewById(R.id.news_detail_like_count);
                                like.setImageResource(R.mipmap.like_selected);
                                like_count.setText("いいね：" + String.valueOf(mNewsDetail.getLikes_count()) + "件");
                                //*****************************************************************************************************

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
                                //******************************************************************************************************

                                //本文*************************************************************************************************
                                TextView introduction = (TextView)findViewById(R.id.news_detail_text);
                                introduction.setText(mNewsDetail.getText());
                                //******************************************************************************************************
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
}
