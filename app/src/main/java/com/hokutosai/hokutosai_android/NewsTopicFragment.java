package com.hokutosai.hokutosai_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * Created by ryoji on 2016/05/05.
 */
public class NewsTopicFragment extends Fragment {

    ArrayList<News> list;
    MyViewPagerAdapter adapter;

    //Volleyでリクエスト時に設定するタグ名。キャンセル時に利用する。
    private static final Object TAG_NEWS_TOPIC_REQUEST_QUEUE = new Object();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<>();
        adapter = new MyViewPagerAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_topic, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(list.isEmpty()) {
            MyJsonArrayRequest jArrayRequest =
                    new MyJsonArrayRequest("https://api.hokutosai.tech/2016/news/topics",
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {

                                    //JSONArrayをListShopItemに変換して取得
                                    Gson gson = new Gson();
                                    Type collectionType = new TypeToken<Collection<News>>() {
                                    }.getType();
                                    list = gson.fromJson(response.toString(), collectionType);

                                    //UIに反映
                                    if(getActivity() != null) {

                                        setTopicView();
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
            jArrayRequest.setTag(TAG_NEWS_TOPIC_REQUEST_QUEUE);    //タグのセット
            RequestQueueSingleton.getInstance().add(jArrayRequest);    //WebAPIの呼び出し
        }
        else{
            setTopicView();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        RequestQueueSingleton.getInstance().cancelAll(TAG_NEWS_TOPIC_REQUEST_QUEUE);
    }

    public void setTopicView(){

        final ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.news_topic_view_pager);
        final LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.news_topic_layout);


        // density (比率)を取得する
        float density = getResources().getDisplayMetrics().density;
        // 21 dp を pixel に変換する ( dp × density + 0.5f（四捨五入) )
        int px = (int) (21f * density + 0.5f);
        int h = layout.getHeight();
        viewPager.getLayoutParams().height = h-px;

        for (int i = 0; i < list.size(); ++i) {
            if( i!=0 && list.get(i).getMedias() != null && !list.get(i).getMedias().isEmpty() && list.get(i).getMedias().get(0).url != null) {  //画像つきのトピックニュース
                NetworkImageView view = new NetworkImageView(getActivity());
                view.setImageUrl(list.get(i).getMedias().get(0).url, ImageLoaderSingleton.getImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()));
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);

                if(i!=0 && list.get(i).getTitle() != null) {
                    final News newsItem = list.get(i);
                    view.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), NewsDetailActivity.class);
                            i.putExtra("News", newsItem);
                            startActivity(i);
                        }
                    });
                }
                adapter.addView(view);

            }
            else if( i!=0 && list.get(i).getTitle() != null){
                TextView view = new TextView(getActivity());
                view.setText(list.get(i).getTitle());
                view.setGravity(Gravity.CENTER);
                view.setTextSize(30);

                if(i!=0 && list.get(i).getTitle() != null) {
                    final News newsItem = list.get(i);
                    view.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity(), NewsDetailActivity.class);
                            i.putExtra("News", newsItem);
                            startActivity(i);
                        }
                    });
                }
                adapter.addView(view);
            }
            else if(i==0){   //画像もタイトルも存在しない一つ目のトピック = テーマ画像
                ImageView image = new ImageView(getActivity());
                image.setImageResource(R.mipmap.hokutosai_2016_theme);
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                adapter.addView(image);
            }
        }
        viewPager.setAdapter(adapter);

        CirclePageIndicator circleIndicator = (CirclePageIndicator)getActivity().findViewById(R.id.news_topic_indicator);
        circleIndicator.setViewPager( viewPager );
    }
}
