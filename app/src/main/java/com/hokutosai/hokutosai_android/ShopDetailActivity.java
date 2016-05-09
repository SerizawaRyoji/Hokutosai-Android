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
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * Created by ryoji on 2016/05/02.
 */
public class ShopDetailActivity extends AppCompatActivity {

    ShopDetail mshopDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("模擬店");

        setContentView(R.layout.activity_shop_detail);

        mshopDetail = new ShopDetail();

        Intent i = getIntent();
        final Shop item = (Shop)i.getSerializableExtra("Shop");

        TextView name = (TextView)this.findViewById(R.id.shop_detail_name);
        TextView tenant = (TextView)this.findViewById(R.id.shop_detail_tenant);
        TextView sales = (TextView)this.findViewById(R.id.shop_detail_sales);

        name.setText(item.getName());
        tenant.setText(item.getTenant());
        sales.setText(item.getSales());

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
                                ImageView like = (ImageView)findViewById(R.id.shop_detail_like);
                                TextView like_count = (TextView)findViewById(R.id.shop_detail_like_count);
                                like.setImageResource(R.mipmap.like_selected);
                                like_count.setText("いいね：" + String.valueOf(mshopDetail.like_count) + "件");
                                //*****************************************************************************************************

                                //メニューの表示******************************************************************************************
                                try {
                                    Gson gsonMenu = new Gson();
                                    JSONArray jArray = response.getJSONArray("menu");    //メニュー取得
                                    //JSONArrayをListShopItemに変換して取得
                                    Type collectionType = new TypeToken<Collection<Menu>>() {
                                    }.getType();


                                   mshopDetail.menus = gsonMenu.fromJson(jArray.toString(), collectionType);
                                    //メニューの読み込みに成功したときのみ反映
                                    TextView menuNameText = (TextView) ShopDetailActivity.this.findViewById(R.id.shop_detail_menu_name);
                                    TextView menuPriceText = (TextView) ShopDetailActivity.this.findViewById(R.id.shop_detail_menu_price);

                                    String menuNameStr = new String();
                                    String menuPriceStr = new String();
                                    for (int i = 0; i < mshopDetail.menus.size(); ++i) {
                                        menuNameStr += mshopDetail.menus.get(i).name;
                                        menuNameStr += "\n";
                                        menuPriceStr += String.valueOf(mshopDetail.menus.get(i).price);
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

    private class ShopDetail{
        int shop_id;
        String name;
        String tenant;
        String sales;
        String image_url;
        AssessedScore assessed_score;
        Boolean liked;
        int like_count;
        String introduction;
        PlaceItem place;
        List<Menu> menus;
    }

    private class Menu{
        int item_id;
        String name;
        int price;
    }

    private class Account{
        String account_id;
        String user_name;
        String media_url;
    }
}
