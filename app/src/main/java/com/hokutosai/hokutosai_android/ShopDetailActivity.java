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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * Created by ryoji on 2016/05/02.
 */
public class ShopDetailActivity extends AppCompatActivity {

    ShopDetail mshopDetail = null;
    int mLikeCount;             //いいねの件数

    //Volleyでリクエスト時に設定するタグ名。キャンセル時に利用する。
    private static final Object TAG_SHOP_LIKE_REQUEST_QUEUE = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("模擬店");

        setContentView(R.layout.activity_shop_detail);

        mshopDetail = new ShopDetail();
        mshopDetail.name = "";

        Intent i = getIntent();
        final Shop item = (Shop)i.getSerializableExtra("Shop");

        mLikeCount = item.getLikes_count();

        TextView name = (TextView)this.findViewById(R.id.shop_detail_name);
        TextView tenant = (TextView)this.findViewById(R.id.shop_detail_tenant);
        TextView sales = (TextView)this.findViewById(R.id.shop_detail_sales);

        name.setText(item.getName());
        tenant.setText(item.getTenant());
        sales.setText(item.getSales());

        setLikeClickEvent(item, this);

        String url = "https://api.hokutosai.tech/2016/shops/";
        url += String.valueOf(item.getShop_id());
        url += "/details";

        MyJsonObjectRequest jObjectRequest =
                new MyJsonObjectRequest(Request.Method.GET,url,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                //JSONArrayをListShopItemに変換して取得
                                Gson gson = new Gson();
                                mshopDetail = gson.fromJson(response.toString(), ShopDetail.class);

                                //画像の表示********************************************************************************************
                                NetworkImageView image = (NetworkImageView) findViewById(R.id.shop_detail_image);
                                int imagewidth = image.getWidth();
                                image.setLayoutParams(new LinearLayout.LayoutParams(imagewidth, imagewidth));
                                image.setImageUrl(item.getImage_url(), ImageLoaderSingleton.getImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()));
                                //******************************************************************************************************


                                //場所の表示*********************************************************************************************
                                TextView place = (TextView)findViewById(R.id.shop_detail_place);
                                place.setText(mshopDetail.place.getName());
                                //*****************************************************************************************************

                                //いいねの表示*******************************************************************************************
                                mLikeCount = mshopDetail.likes_count;
                                TextView like_count = (TextView)findViewById(R.id.shop_detail_like_count);
                                like_count.setText("いいね：" + String.valueOf(mLikeCount) + "件");
                                //*****************************************************************************************************

                                //いいね(クリック可)の表示********************************************************************************
                                ImageView like = (ImageView)findViewById(R.id.shop_detail_like_clickable);
                                like.setSelected(mshopDetail.liked);
                                //*****************************************************************************************************

                                //メニューの表示*****************************************************************************************
                                try {
                                    Gson gsonMenu = new Gson();
                                    JSONArray jArray = response.getJSONArray("menu");    //メニュー取得
                                    //JSONArrayをListShopItemに変換して取得
                                    Type collectionType = new TypeToken<Collection<ShopDetailActivity.Menu>>() {
                                    }.getType();

                                   mshopDetail.menu = gsonMenu.fromJson(jArray.toString(), collectionType);
                                    //メニューの読み込みに成功したときのみ反映
                                    TextView menuNameText = (TextView) ShopDetailActivity.this.findViewById(R.id.shop_detail_menu_name);
                                    TextView menuPriceText = (TextView) ShopDetailActivity.this.findViewById(R.id.shop_detail_menu_price);

                                    String menuNameStr = new String();
                                    String menuPriceStr = new String();
                                    for (int i = 0; i < mshopDetail.menu.size(); ++i) {
                                        menuNameStr += mshopDetail.menu.get(i).name;
                                        menuNameStr += "\n";
                                        menuPriceStr += String.valueOf(mshopDetail.menu.get(i).price);
                                        menuPriceStr += "円\n";
                                    }
                                    menuNameText.setText(menuNameStr);
                                    menuPriceText.setText(menuPriceStr);

                                } catch (JSONException e) {    //menuがnullの時など
                                    // TODO 自動生成された catch ブロック
                                    e.printStackTrace();
                                }
                                //******************************************************************************************************

                                //説明文*************************************************************************************************
                                TextView introduction = (TextView)findViewById(R.id.shop_detail_introduction);
                                introduction.setText(mshopDetail.introduction);
                                //******************************************************************************************************

                                //評価***************************************************************************************************
                                float rate = mshopDetail.assessment_aggregate.getTotal_score() / (float) mshopDetail.assessment_aggregate.getAssessed_count();
                                RatingBar allRate = (RatingBar)findViewById(R.id.shop_detail_all_rate);
                                allRate.setRating( rate );
                                TextView allRateString = (TextView)findViewById(R.id.shop_detail_all_rate_str);
                                allRateString.setText( "(" + String.format("%.2f", rate) + ")" );
                                TextView allRateNum = (TextView)findViewById(R.id.shop_detail_all_rate_num);
                                allRateNum.setText("評価件数：" + mshopDetail.assessment_aggregate.getAssessed_count());
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

    @Override
    protected void onStop() {
        super.onStop();
        RequestQueueSingleton.getInstance().cancelAll(TAG_SHOP_LIKE_REQUEST_QUEUE);
    }

    private void setLikeClickEvent(final Shop item, final Activity activity){

        final ImageView like = (ImageView)findViewById(R.id.shop_detail_like_clickable);
        // ImageViewオブジェクトにクリックイベントを追加する
        if(like != null) {
            like.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {
                            String url = "https://api.hokutosai.tech/2016/shops/" + item.getShop_id() + "/likes";
                            int method = Request.Method.POST;
                            if(item.getLiked()) method = Request.Method.DELETE; //いいね済みの状態でいいねを押した場合はDELETE

                            MyStringRequest postJson = new MyStringRequest(method, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // TODO 自動生成されたメソッド・スタブ
                                            if( activity != null) {
                                                like.setSelected(!like.isSelected());   //画像の反転

                                                TextView like_count = (TextView)findViewById(R.id.shop_detail_like_count);

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
                            postJson.setTag(TAG_SHOP_LIKE_REQUEST_QUEUE);    //タグのセット
                            RequestQueueSingleton.getInstance().add(postJson);    //WebAPIの呼び出し
                        }
                    }
            );
        }
    }

    public void reviewShowClickResult( View view ){

        if( !mshopDetail.name.isEmpty() ) {
            Intent i = new Intent(ShopDetailActivity.this, ShopReviewActivity.class);
            i.putExtra("ShopDetail", mshopDetail);
            startActivity(i);
        }
    }

    public void reviewWriteClickResult( View view ){

        Log.d("test","wqwq");
    }

    public class ShopDetail implements Serializable {
        int shop_id;
        String name;
        String tenant;
        String sales;
        String image_url;
        AssessedScore assessment_aggregate;
        Boolean liked;
        int likes_count;
        String introduction;
        Place place;
        List<Menu> menu;
        List<Assessment> assessments;
        Assessment my_asssessment;
    }

    public class Menu implements Serializable{
        int item_id;
        int price;
        String name;
    }
}
