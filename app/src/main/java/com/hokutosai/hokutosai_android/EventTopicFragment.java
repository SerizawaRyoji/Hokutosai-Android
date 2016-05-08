package com.hokutosai.hokutosai_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ryoji on 2016/05/04.
 */
public class EventTopicFragment extends Fragment {

    ArrayList<Event> list;
    MyViewPagerAdapter adapter;

    //Volleyでリクエスト時に設定するタグ名。キャンセル時に利用する。
    private static final Object TAG_EVENT_TOPIC_REQUEST_QUEUE = new Object();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<>();
        adapter = new MyViewPagerAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_event_topic, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(list.isEmpty()) {
            MyJsonArrayRequest jArrayRequest =
                    new MyJsonArrayRequest("https://api.hokutosai.tech/2016/events/topics",
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    //JSONArrayをListShopItemに変換して取得
                                    Gson gson = new Gson();
                                    Type collectionType = new TypeToken<Collection<Event>>() {
                                    }.getType();
                                    list = gson.fromJson(response.toString(), collectionType);

                                    //UIに反映
                                    if(getActivity() != null) {
                                        setTopicVIew();
                                    }
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
            jArrayRequest.setTag(TAG_EVENT_TOPIC_REQUEST_QUEUE);    //タグのセット
            RequestQueueSingleton.getInstance().add(jArrayRequest);    //WebAPIの呼び出し
        }
        else{
            setTopicVIew();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        RequestQueueSingleton.getInstance().cancelAll(TAG_EVENT_TOPIC_REQUEST_QUEUE);
    }

    void setTopicVIew(){

        final ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.event_topic_view_pager);

        for (int i = 0; i < list.size(); ++i) {
            NetworkImageView view = new NetworkImageView(getActivity());
            view.setImageUrl(list.get(i).getImage_url(), ImageLoaderSingleton.getImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()));
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);

            final Event eventItem = list.get(i);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), EventDetailActivity.class);
                    i.putExtra("Event", eventItem);
                    startActivity(i);
                }
            });

            adapter.addView(view);
        }
        viewPager.setAdapter(adapter);

        CirclePageIndicator circleIndicator = (CirclePageIndicator)getActivity().findViewById(R.id.event_topic_indicator);
        circleIndicator.setViewPager( viewPager );
    }
}
