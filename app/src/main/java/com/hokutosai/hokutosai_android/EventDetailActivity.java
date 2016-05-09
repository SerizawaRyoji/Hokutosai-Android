package com.hokutosai.hokutosai_android;

import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONObject;

/**
 * Created by ryoji on 2016/05/05.
 */
public class EventDetailActivity extends AppCompatActivity {

    EventDetail mEventDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("イベント");

        setContentView(R.layout.activity_event_detail);

        mEventDetail = new EventDetail();

        Intent i = getIntent();
        final Event item = (Event)i.getSerializableExtra("Event");

        String url = "https://api.hokutosai.tech/2016/events/";
        url += String.valueOf(item.getEvent_id());
        url += "/details";

        MyJsonObjectRequest jObjectRequest =
                new MyJsonObjectRequest(Request.Method.GET,url,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                //JSONArrayをListShopItemに変換して取得
                                Gson gson = new Gson();
                                mEventDetail = gson.fromJson(response.toString(), EventDetail.class);

                                //画像の表示********************************************************************************************
                                NetworkImageView image = (NetworkImageView) findViewById(R.id.event_detail_image);
                                image.setImageUrl(mEventDetail.image_url, ImageLoaderSingleton.getImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()));
                                //****************************************************************************************************

                                //タイトルの表示****************************************************************************************
                                TextView title = (TextView)findViewById(R.id.event_detail_name);
                                title.setText(mEventDetail.title);
                                //****************************************************************************************************

                                //出演者の表示******************************************************************************************
                                TextView performer = (TextView)findViewById(R.id.event_detail_performer);
                                performer.setText(mEventDetail.performer);
                                //****************************************************************************************************

                                //場所の表示*******************************************************************************************
                                TextView place = (TextView)findViewById(R.id.event_detail_place);
                                place.setText(mEventDetail.place.getName());
                                //****************************************************************************************************

                                //いいねの表示******************************************************************************************
                                ImageView like = (ImageView)findViewById(R.id.event_detail_like);
                                TextView like_count = (TextView)findViewById(R.id.event_detail_like_count);
                                like.setImageResource(R.mipmap.like_selected);
                                like_count.setText("いいね：" + String.valueOf(mEventDetail.like_count) + "件");
                                //****************************************************************************************************

                                //詳細の表示*******************************************************************************************
                                TextView detail = (TextView)findViewById(R.id.event_detail_detail);
                                place.setText(mEventDetail.detail);
                                //****************************************************************************************************
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

    private class EventDetail{
        int event_id;
        String title;
        String date;
        String start_time;
        String end_time;
        PlaceItem place;
        String performer;
        String detail;
        String image_url;
        int like_count;
        Boolean liked;
        Boolean featured;
    }
}
