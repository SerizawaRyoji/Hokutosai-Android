package com.hokutosai.hokutosai_android;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ryoji on 2016/05/12.
 */
public class MyDateFormatSingleton{

    // このクラスに唯一のインスタンス
    private static MyDateFormatSingleton instance = new MyDateFormatSingleton();

    private SimpleDateFormat mInputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private SimpleDateFormat mOutputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    private MyDateFormatSingleton() {}

    // インスタンス取得メソッド
    public static MyDateFormatSingleton getInstance() {
        return instance;
    }

    public String getDateTIme( String datetime){

        try {
            Date date = mInputFormat.parse(datetime);
            return mOutputFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return datetime;
        }

    }
}
