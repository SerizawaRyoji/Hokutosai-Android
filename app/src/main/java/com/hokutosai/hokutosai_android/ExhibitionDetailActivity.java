package com.hokutosai.hokutosai_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by ryoji on 2016/05/05.
 */
public class ExhibitionDetailActivity extends AppCompatActivity {

    private ExhibitionDetail mExhibitionDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("展示");

        setContentView(R.layout.activity_exhibition_detail);

        mExhibitionDetail = new ExhibitionDetail();

        Intent i = getIntent();
        final Exhibition item = (Exhibition)i.getSerializableExtra("Exhibition");

        TextView name = (TextView)this.findViewById(R.id.exhibition_detail_name);
        TextView exhibitors = (TextView)this.findViewById(R.id.exhibition_detail_exhibitors);
        TextView displays = (TextView)this.findViewById(R.id.exhibition_detail_displays);

        name.setText(item.getTitle());
        exhibitors.setText(item.getExhibitors());
        displays.setText(item.getDisplays());

        String url = "https://api.hokutosai.tech/2016/exhibitions/";
        url += String.valueOf(item.getExhibition_id());
        url += "/details";

        MyJsonObjectRequest jObjectRequest =
                new MyJsonObjectRequest(Request.Method.GET,url,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                //JSONArrayをListShopItemに変換して取得
                                Gson gson = new Gson();
                                mExhibitionDetail = gson.fromJson(response.toString(), ExhibitionDetail.class);

                                //画像の表示********************************************************************************************
                                NetworkImageView image = (NetworkImageView) findViewById(R.id.exhibition_detail_image);
                                int imagewidth = image.getWidth();
                                image.setLayoutParams(new LinearLayout.LayoutParams(imagewidth, imagewidth));
                                image.setImageUrl(item.getImage_url(), ImageLoaderSingleton.getImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()));
                                //******************************************************************************************************


                                //場所の表示*********************************************************************************************
                                TextView place = (TextView)findViewById(R.id.exhibition_detail_place);
                                place.setText(mExhibitionDetail.place.getName());
                                //*****************************************************************************************************

                                //いいねの表示*******************************************************************************************
                                ImageView like = (ImageView)findViewById(R.id.exhibition_detail_like);
                                TextView like_count = (TextView)findViewById(R.id.exhibition_detail_like_count);
                                like.setImageResource(R.drawable.like_selected);
                                like_count.setText("いいね：" + String.valueOf(mExhibitionDetail.likes_count) + "件");
                                //*****************************************************************************************************

                                //説明文*************************************************************************************************
                                TextView introduction = (TextView)findViewById(R.id.exhibition_detail_introduction);
                                introduction.setText(mExhibitionDetail.introduction);
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

    private class ExhibitionDetail{
        int exhibition_id;
        String title;
        String exhibitors;
        String displays;
        String image_url;
        AssessedScore assessed_score;
        Boolean liked;
        int likes_count;
        String introduction;
        PlaceItem place;
        List<Assessment> assessments;
        Assessment my_assessment;
    }
}
