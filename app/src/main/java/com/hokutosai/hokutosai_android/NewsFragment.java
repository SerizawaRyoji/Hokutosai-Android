package com.hokutosai.hokutosai_android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ryoji on 2016/05/05.
 */
public class NewsFragment extends Fragment {

    //ArrayList<NewsItem> list;
    //ListView listView;

    //Volleyでリクエスト時に設定するタグ名。キャンセル時に利用する。
    private static final Object TAG_NEWS_REQUEST_QUEUE = new Object();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //list = new ArrayList<>();
        //listView = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*String url = "https://api.hokutosai.tech/2016/news/timeline";

        if(list.isEmpty()) {
            MyJsonArrayRequest jArrayRequest =
                    new MyJsonArrayRequest(url,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    //JSONArrayをListShopItemに変換して取得
                                    Gson gson = new Gson();
                                    Type collectionType = new TypeToken<Collection<NewsItem>>() {
                                    }.getType();
                                    list = gson.fromJson(response.toString(), collectionType);
                                    Log.d("test",list.get(0).text);

                                    //UIに反映
                                    listView = (ListView) getActivity().findViewById(R.id.list_news_view);
                                    adapter.setNewsItemList(list);
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
            jArrayRequest.setTag(TAG_NEWS_REQUEST_QUEUE);    //タグのセット
            RequestQueueSingleton.getInstance().add(jArrayRequest);    //WebAPIの呼び出し
        }
        else{
            listView = (ListView) getActivity().findViewById(R.id.list_news_view);
            adapter.setNewsItemList(list);
            listView.setAdapter(adapter);

            //setClickListener();     //クリックしたときの処理について
        }*/
    }
}
