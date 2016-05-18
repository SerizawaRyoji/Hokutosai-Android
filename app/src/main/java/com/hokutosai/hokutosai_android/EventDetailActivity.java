package com.hokutosai.hokutosai_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ryoji on 2016/05/05.
 */
public class EventDetailActivity extends AppCompatActivity {

    EventDetail mEventDetail;
    int mLikeCount;             //いいねの件数

    //Volleyでリクエスト時に設定するタグ名。キャンセル時に利用する。
    private static final Object TAG_EVENT_LIKE_REQUEST_QUEUE = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("イベント");

        setContentView(R.layout.activity_event_detail);

        mEventDetail = new EventDetail();

        Intent i = getIntent();
        final Event item = (Event)i.getSerializableExtra("Event");

        mLikeCount = item.getLikes_count();

        setLikeClickEvent(item, this);

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

                                if(mEventDetail.liked == null) mEventDetail.liked = false;

                                //画像の表示********************************************************************************************
                                NetworkImageView image = (NetworkImageView) findViewById(R.id.event_detail_image);
                                int w = image.getWidth();
                                int h = image.getWidth() * 2 / 5;
                                image.setLayoutParams(new LinearLayout.LayoutParams(w,h));
                                image.setImageUrl(mEventDetail.image_url, ImageLoaderSingleton.getImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()));
                                //****************************************************************************************************

                                //タイトルの表示****************************************************************************************
                                TextView title = (TextView)findViewById(R.id.event_detail_name);
                                if( !mEventDetail.title.isEmpty() ) title.setText(mEventDetail.title);
                                else title.setText("未登録");
                                //****************************************************************************************************

                                //出演者の表示******************************************************************************************
                                TextView performer = (TextView)findViewById(R.id.event_detail_performer);
                                if(!mEventDetail.performer.isEmpty()) performer.setText(mEventDetail.performer);
                                else performer.setText("未登録");
                                //****************************************************************************************************

                                //場所の表示*******************************************************************************************
                                TextView place = (TextView)findViewById(R.id.event_detail_place);
                                place.setText(mEventDetail.place.getName());
                                //****************************************************************************************************

                                //時間の表示*******************************************************************************************
                                TextView datetime = (TextView)findViewById(R.id.event_detail_datetime);
                                if(!mEventDetail.start_time.isEmpty() && !mEventDetail.end_time.isEmpty()){
                                    datetime.setText(MyDateFormatSingleton.getInstance().getEventDateTime(mEventDetail.date,mEventDetail.start_time,mEventDetail.end_time));
                                }
                                else datetime.setText("未登録");
                                //****************************************************************************************************

                                //いいねの表示******************************************************************************************
                                mLikeCount = mEventDetail.likes_count;
                                TextView like_count = (TextView)findViewById(R.id.event_detail_like_count);
                                like_count.setText("いいね：" + String.valueOf(mLikeCount) + "件");
                                //****************************************************************************************************

                                //いいね(クリック可)の表示********************************************************************************
                                ImageView like = (ImageView)findViewById(R.id.event_detail_like_clickable);
                                like.setSelected(mEventDetail.liked);
                                //*****************************************************************************************************

                                //詳細の表示*******************************************************************************************
                                TextView detail = (TextView)findViewById(R.id.event_detail_detail);
                                detail.setText(mEventDetail.detail);
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

                // intentの作成
                Intent intent = new Intent();
                // intentへ添え字付で値を保持させる
                mEventDetail.likes_count = mLikeCount;
                intent.putExtra( "EventDetail", mEventDetail );
                // 返却したい結果ステータスをセットする
                setResult( Activity.RESULT_OK, intent );

                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        // intentの作成
        Intent intent = new Intent();
        // intentへ添え字付で値を保持させる
        mEventDetail.likes_count = mLikeCount;
        intent.putExtra( "EventDetail", mEventDetail );
        // 返却したい結果ステータスをセットする
        setResult( Activity.RESULT_OK, intent );

        finish();

        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
        RequestQueueSingleton.getInstance().cancelAll(TAG_EVENT_LIKE_REQUEST_QUEUE);
    }

    private void setLikeClickEvent(final Event item, final Activity activity){

        final ImageView like = (ImageView)findViewById(R.id.event_detail_like_clickable);
        // ImageViewオブジェクトにクリックイベントを追加する
        if(like != null) {
            like.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {

                            if(item.getLiked() == null) return;

                            String url = "https://api.hokutosai.tech/2016/events/" + item.getEvent_id() + "/likes";
                            int method = Request.Method.POST;
                            if(item.getLiked()) method = Request.Method.DELETE; //いいね済みの状態でいいねを押した場合はDELETE

                            MyStringRequest postJson = new MyStringRequest(method, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // TODO 自動生成されたメソッド・スタブ
                                            if( activity != null) {
                                                like.setSelected(!like.isSelected());   //画像の反転
                                                mEventDetail.liked = !mEventDetail.liked;

                                                TextView like_count = (TextView)findViewById(R.id.event_detail_like_count);

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
                            postJson.setTag(TAG_EVENT_LIKE_REQUEST_QUEUE);    //タグのセット
                            RequestQueueSingleton.getInstance().add(postJson);    //WebAPIの呼び出し
                        }
                    }
            );
        }
    }

    public class EventDetail implements Serializable{
        int event_id;
        String title;
        String date;
        String start_time;
        String end_time;
        Place place;
        String performer;
        String detail;
        String image_url;
        int likes_count;
        Boolean liked;
        Boolean featured;
    }
}
