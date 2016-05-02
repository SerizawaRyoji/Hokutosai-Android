package com.hokutosai.hokutosai_android;

import android.app.Application;

/**
 * Created by ryoji on 2016/05/02.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        // TODO 自動生成されたメソッド・スタブ
        super.onCreate();

        RequestQueueSingleton.create(getApplicationContext());    //RequestQueueの作成
    }
}
