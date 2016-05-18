package com.hokutosai.hokutosai_android;

import android.app.Application;
import android.graphics.Bitmap;

/**
 * Created by ryoji on 2016/05/02.
 */
public class MyApplication extends Application {

    private static MyApplication sInstance;

    private final String TAG = "APPLICATION";
    private Bitmap bmp;

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

    public void setBmp(Bitmap bmp){
        this.bmp = bmp;
    }

    public Bitmap getBmp(){
        return bmp;
    }

    public void clearBmp(){
        bmp = null;
    }
}
