package com.hokutosai.hokutosai_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;

/**
 * Created by ryoji on 2016/05/08.
 */
public class NewsDetailActivity extends Activity {

    News mNewsDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_detail);

        mNewsDetail = new News();

        Intent i = getIntent();
        final News item = (News)i.getSerializableExtra("News");

        TextView title = (TextView)this.findViewById(R.id.news_detail_title);
        title.setText(item.getTitle());

        String url = "https://api.hokutosai.tech/2016/news/";
        url += String.valueOf(item.getNews_id());
        url += "/details";

        MyJsonObjectRequest jObjectRequest =
                new MyJsonObjectRequest(Request.Method.GET,url,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

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

                                    //メニューの読み込みに成功したときのみ反映
                                    TextView UrlList = (TextView) NewsDetailActivity.this.findViewById(R.id.news_detail_imagelist);

                                    String mediasUrl = "";
                                    for (int i = 0; i < mNewsDetail.medias.size(); ++i) {
                                        Log.d("test",mNewsDetail.medias.get(i).url);
                                        mediasUrl += mNewsDetail.medias.get(i).url + '\n';
                                    }
                                    UrlList.setText(mediasUrl);

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
}
