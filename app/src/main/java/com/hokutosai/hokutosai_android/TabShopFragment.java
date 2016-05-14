package com.hokutosai.hokutosai_android;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ryoji on 2016/05/01.
 */
public class TabShopFragment  extends Fragment {

    ArrayList<Shop> list;
    ShopItemAdapter adapter;
    ListView listView;
    Boolean isFirst;
    Boolean mRequestEnded;
    Boolean isStoped;

    //Volleyでリクエスト時に設定するタグ名。キャンセル時に利用する。
    private static final Object TAG_SHOP_REQUEST_QUEUE = new Object();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<>();
        adapter = new ShopItemAdapter( getActivity() );
        listView = null;
        isFirst = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO 自動生成されたメソッド・スタブ
        return inflater.inflate(R.layout.tab_shop_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if(list.isEmpty()) {
            loadShopList();     //リストをロードしてアダプターにセット
        }
        else{
            listView = (ListView) getActivity().findViewById(R.id.list_shop_view);
            adapter.setShopList(list);
            listView.setAdapter(adapter);

            setClickListener();     //クリックしたときの処理について
        }
    }

    private void setClickListener(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                Intent i = new Intent(getActivity(), ShopDetailActivity.class);
                i.putExtra("Shop", list.get(position));
                startActivity(i);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        isStoped = true;
        RequestQueueSingleton.getInstance().cancelAll(TAG_SHOP_REQUEST_QUEUE);
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    loadShopList(); //サーバーからショップリストを受け取る

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
        }).start();
    }

    public void loadShopList(){

        mRequestEnded = false;

        MyJsonArrayRequest jArrayRequest =
                new MyJsonArrayRequest("https://api.hokutosai.tech/2016/shops/",
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                //JSONArrayをListShopItemに変換して取得
                                Gson gson = new Gson();
                                Type collectionType = new TypeToken<Collection<Shop>>() {
                                }.getType();
                                list = gson.fromJson(response.toString(), collectionType);

                                if(getActivity() != null) {
                                    //UIに反映
                                    adapter.setShopList(list);

                                    //salesの分の幅を計算
                                    WindowManager wm = (WindowManager)getActivity().getSystemService(getActivity().WINDOW_SERVICE);
                                    Display disp = wm.getDefaultDisplay();
                                    Point size = new Point();
                                    disp.getSize(size);         //画面のディスプレイサイズを取得(ピクセル)
                                    int width =  getActivity().getResources().getDimensionPixelSize(R.dimen.list_shop_image_size)    //ショップ画像といいねの画像の幅を足し合わせる
                                            + getActivity().getResources().getDimensionPixelSize(R.dimen.list_shop_item_height);
                                    int px = size.x - (width + 240);  //画面幅 - ( ショップ画像幅+いいね画像幅+いいね数の文字列幅(240)) がsales(TextView)の幅となる   ＊240は本当はちゃんと計算したほうがいい
                                    adapter.setSalesWidth(px);

                                    if(listView == null){   //初めてリストを作成するときのみ呼ぶ
                                        listView = (ListView) getActivity().findViewById(R.id.list_shop_view);
                                        listView.setAdapter(adapter);
                                        setClickListener();     //クリックしたときの処理について
                                    }
                                }
                                mRequestEnded = true;
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
        jArrayRequest.setTag(TAG_SHOP_REQUEST_QUEUE);    //タグのセット
        RequestQueueSingleton.getInstance().add(jArrayRequest);    //WebAPIの呼び出し
    }
}
