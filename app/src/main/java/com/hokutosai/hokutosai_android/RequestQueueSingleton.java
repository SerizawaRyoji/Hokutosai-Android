package com.hokutosai.hokutosai_android;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ryoji on 2016/05/02.
 */
public class RequestQueueSingleton extends RequestQueue {

    private static RequestQueue instance = null;

    public RequestQueueSingleton(Cache cache, Network network) {
        super(cache, network);
        // TODO 自動生成されたコンストラクター・スタブ
    }

    public static void create(Context context){
        if(instance == null){
            instance = Volley.newRequestQueue(context);
        }
    }

    public static RequestQueue getInstance(){
        return instance;
    }
}
