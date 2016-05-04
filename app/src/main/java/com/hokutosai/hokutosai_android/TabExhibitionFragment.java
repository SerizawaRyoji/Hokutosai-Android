package com.hokutosai.hokutosai_android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class TabExhibitionFragment  extends Fragment {

    ArrayList<Exhibition> list;
    ExhibitionItemAdapter adapter;
    ListView listView;

    //Volleyでリクエスト時に設定するタグ名。キャンセル時に利用する。
    private static final Object TAG_EXHIBITION_REQUEST_QUEUE = new Object();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO 自動生成されたメソッド・スタブ
        super.onCreate(savedInstanceState);

        list = new ArrayList<>();
        adapter = new ExhibitionItemAdapter( getActivity() );
        listView = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO 自動生成されたメソッド・スタブ
        return inflater.inflate(R.layout.tab_exhibition_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(list.isEmpty()) {
            MyJsonArrayRequest jArrayRequest =
                    new MyJsonArrayRequest("https://api.hokutosai.tech/2016/exhibitions",
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    //JSONArrayをListShopItemに変換して取得
                                    Gson gson = new Gson();
                                    Type collectionType = new TypeToken<Collection<Exhibition>>() {
                                    }.getType();
                                    list = gson.fromJson(response.toString(), collectionType);

                                    //UIに反映
                                    listView = (ListView) getActivity().findViewById(R.id.list_exhibition_view);
                                    adapter.setExhibitionList(list);
                                    listView.setAdapter(adapter);

                                    //setClickListener();     //クリックしたときの処理について
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
            jArrayRequest.setTag(TAG_EXHIBITION_REQUEST_QUEUE);    //タグのセット
            RequestQueueSingleton.getInstance().add(jArrayRequest);    //WebAPIの呼び出し
        }
        else{
            listView = (ListView) getActivity().findViewById(R.id.list_exhibition_view);
            adapter.setExhibitionList(list);
            listView.setAdapter(adapter);

            //setClickListener();     //クリックしたときの処理について
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        RequestQueueSingleton.getInstance().cancelAll(TAG_EXHIBITION_REQUEST_QUEUE);
    }
}