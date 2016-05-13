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

    private SimpleDateFormat mInputDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private SimpleDateFormat mInputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat mInputTimeFormat = new SimpleDateFormat("HH:mm:ss");

    private SimpleDateFormat mOutputDateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private SimpleDateFormat mOutputDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private SimpleDateFormat mOutputTimeFormat = new SimpleDateFormat("HH:mm");

    private MyDateFormatSingleton() {}

    // インスタンス取得メソッド
    public static MyDateFormatSingleton getInstance() {
        return instance;
    }

    public String getDateTime( String datetime ){

        try {
            Date date = mInputDateTimeFormat.parse(datetime);
            return mOutputDateTimeFormat.format(date);

        } catch (ParseException e) {
            e.printStackTrace();
            return datetime;
        }
    }

    public String getEventDateTime(String date, String start_time, String end_time){
        try {
            Date d = mInputDateFormat.parse(date);
            Date st = mInputTimeFormat.parse(start_time);
            Date et = mInputTimeFormat.parse(end_time);

            return mOutputDateFormat.format(d) + " " + mOutputTimeFormat.format(st) + "-" + mOutputTimeFormat.format(et);

        } catch (ParseException e) {
            e.printStackTrace();
            return date + start_time + end_time;
        }
    }

    public long getDiffMinutes(Date now, String date, String time){

        try {

            Date d = mInputDateFormat.parse(date);
            Date t = mInputTimeFormat.parse(time);

            //now,day:世界標準時 time:日本標準時
            long d1 = now.getTime();
            long d2 = d.getTime() + ( t.getTime() + 9*60*60*1000) ;             //timeは世界標準時と合わせる必要がある
            return (d2 - d1) / (1000 * 60);                                     //[ms]->[min]

        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }



    }
}
