package com.hokutosai.hokutosai_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    ArrayList<ShopItem> list;
    ShopItemAdapter adapter;
    ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<>();
        adapter = new ShopItemAdapter( getActivity() );
        listView = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*listView = (ListView) getActivity().findViewById(R.id.list_shop_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                ListView lv = (ListView) parent;
                // クリックされたアイテムを取得します
                ShopItem item = (ShopItem) lv.getItemAtPosition(position);
                Log.d("test",item.getName());
            }
        });*/

        // TODO 自動生成されたメソッド・スタブ
        return inflater.inflate(R.layout.tab_shop_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        //adapter.setShopItemList(list);
        //listView.setAdapter(adapter);


        if(list.isEmpty()) {
            MyJsonArrayRequest jArrayRequest =
                    new MyJsonArrayRequest("https://api.hokutosai.tech/2016/shops/",
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    //JSONArrayをListShopItemに変換して取得
                                    Gson gson = new Gson();
                                    Type collectionType = new TypeToken<Collection<ShopItem>>() {
                                    }.getType();
                                    list = gson.fromJson(response.toString(), collectionType);

                                    //UIに反映
                                    listView = (ListView) getActivity().findViewById(R.id.list_shop_view);
                                    adapter.setShopItemList(list);
                                    listView.setAdapter(adapter);

                                    setClickListener();     //クリックしたときの処理について
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
            RequestQueueSingleton.getInstance().add(jArrayRequest);    //WebAPIの呼び出し
        }
        else{
            listView = (ListView) getActivity().findViewById(R.id.list_shop_view);
            adapter.setShopItemList(list);
            listView.setAdapter(adapter);

            setClickListener();     //クリックしたときの処理について
        }
    }

    private void setClickListener(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                //ListView lv = (ListView) parent;
                // クリックされたアイテムを取得します
                //ShopItem item = (ShopItem) lv.getItemAtPosition(position);
                //Log.d("test",item.getName());

                Intent i = new Intent(getActivity(), ShopDetailActivity.class);
                i.putExtra("ShopItem", list.get(position));
                startActivity(i);
            }
        });
    }
}
