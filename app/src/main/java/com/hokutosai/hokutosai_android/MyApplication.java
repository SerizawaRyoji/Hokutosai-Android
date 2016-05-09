package com.hokutosai.hokutosai_android;

import android.app.Application;

/**
 * Created by ryoji on 2016/05/02.
 */
public class MyApplication extends Application {

    private static MyApplication sInstance;

    public static synchronized MyApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        // TODO 自動生成されたメソッド・スタブ
        super.onCreate();
        sInstance = this;

        RequestQueueSingleton.create(getApplicationContext());    //RequestQueueの作成
        LruCacheSingleton.create();

        MyAccountSingleton.getInstance().createAccount(this);   //アカウントの作成
    }
}
