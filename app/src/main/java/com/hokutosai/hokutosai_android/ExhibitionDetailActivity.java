package com.hokutosai.hokutosai_android;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by ryoji on 2016/05/05.
 */
public class ExhibitionDetailActivity extends AppCompatActivity {

    private ExhibitionDetail mExhibitionDetail;
    int mLikeCount;             //いいねの件数

    //Volleyでリクエスト時に設定するタグ名。キャンセル時に利用する。
    private static final Object TAG_EXHIBITION_LIKE_REQUEST_QUEUE = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("展示");

        setContentView(R.layout.activity_exhibition_detail);

        mExhibitionDetail = new ExhibitionDetail();
        mExhibitionDetail.title = "";

        Intent i = getIntent();
        final Exhibition item = (Exhibition)i.getSerializableExtra("Exhibition");

        mLikeCount = item.getLikes_count();

        TextView name = (TextView)this.findViewById(R.id.exhibition_detail_name);
        TextView exhibitors = (TextView)this.findViewById(R.id.exhibition_detail_exhibitors);
        TextView displays = (TextView)this.findViewById(R.id.exhibition_detail_displays);

        name.setText(item.getTitle());
        exhibitors.setText(item.getExhibitors());
        displays.setText(item.getDisplays());

        setLikeClickEvent(item, this);

        String url = "https://api.hokutosai.tech/2016/exhibitions/";
        url += String.valueOf(item.getExhibition_id());
        url += "/details";

        MyJsonObjectRequest jObjectRequest =
                new MyJsonObjectRequest(Request.Method.GET,url,null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Gson gson = new Gson();
                                mExhibitionDetail = gson.fromJson(response.toString(), ExhibitionDetail.class);

                                if(mExhibitionDetail.liked == null) mExhibitionDetail.liked = false;

                                //画像の表示********************************************************************************************
                                NetworkImageView image = (NetworkImageView) findViewById(R.id.exhibition_detail_image);
                                int imagewidth = image.getWidth();
                                image.setLayoutParams(new LinearLayout.LayoutParams(imagewidth, imagewidth));
                                image.setImageUrl(item.getImage_url(), ImageLoaderSingleton.getImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()));
                                //******************************************************************************************************


                                //場所の表示*********************************************************************************************
                                TextView place = (TextView)findViewById(R.id.exhibition_detail_place);
                                place.setText(mExhibitionDetail.place.getName());
                                //*****************************************************************************************************

                                //いいねの表示*******************************************************************************************
                                mLikeCount = mExhibitionDetail.likes_count;
                                TextView like_count = (TextView)findViewById(R.id.exhibition_detail_like_count);
                                like_count.setText("いいね：" + String.valueOf(mLikeCount) + "件");
                                //*****************************************************************************************************

                                //いいね(クリック可)の表示********************************************************************************
                                ImageView like = (ImageView)findViewById(R.id.exhibition_detail_like_clickable);
                                like.setSelected(mExhibitionDetail.liked);
                                //*****************************************************************************************************

                                //説明文*************************************************************************************************
                                TextView introduction = (TextView)findViewById(R.id.exhibition_detail_introduction);
                                introduction.setText(mExhibitionDetail.introduction);
                                //******************************************************************************************************

                                //評価***************************************************************************************************
                                float rate = mExhibitionDetail.assessment_aggregate.getTotal_score() / (float) mExhibitionDetail.assessment_aggregate.getAssessed_count();
                                if(Float.isNaN(rate)) rate = 0;
                                RatingBar allRate = (RatingBar)findViewById(R.id.exhibition_detail_all_rate);
                                allRate.setRating( rate );
                                TextView allRateString = (TextView)findViewById(R.id.exhibition_detail_all_rate_str);
                                allRateString.setText( "(" + String.format("%.2f", rate) + ")" );
                                TextView allRateNum = (TextView)findViewById(R.id.exhibition_detail_all_rate_num);
                                allRateNum.setText("評価件数：" + mExhibitionDetail.assessment_aggregate.getAssessed_count());
                                //******************************************************************************************************

                                //レビュー***********************************************************************************************
                                TextView showText = (TextView)ExhibitionDetailActivity.this.findViewById(R.id.exhibition_detail_show_review);
                                TextView reviewText = (TextView)ExhibitionDetailActivity.this.findViewById(R.id.exhibition_detail_write_review);
                                TextView deleteText = (TextView)ExhibitionDetailActivity.this.findViewById(R.id.exhibition_detail_delete_review);

                                showText.setVisibility(View.VISIBLE);
                                deleteText.setVisibility(View.VISIBLE);

                                if(mExhibitionDetail.my_assessment == null){
                                    reviewText.setText("レビューを書く");
                                    deleteText.setClickable(false);
                                    deleteText.setTextColor(getResources().getColor(R.color.ClickdisableText));
                                }
                                else{
                                    Log.d("test", mExhibitionDetail.my_assessment.toString());
                                    reviewText.setText("自分のレビューを修正する");
                                    deleteText.setClickable(true);  //レビュー削除ボタンを有効に
                                    deleteText.setTextColor(getResources().getColor(R.color.text_clickable));
                                }
                                //******************************************************************************************************
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

        jObjectRequest.setCustomTimeOut();   //タイムアウト時間の変更
        RequestQueueSingleton.getInstance().add(jObjectRequest);    //WebAPIの呼び出し
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){ // if使うとエラー（itemがInt形式なため）
            case android.R.id.home:   // アプリアイコン（ホームアイコン）を押した時の処理
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        RequestQueueSingleton.getInstance().cancelAll(TAG_EXHIBITION_LIKE_REQUEST_QUEUE);
    }

    private void setLikeClickEvent(final Exhibition item, final Activity activity){

        final ImageView like = (ImageView)findViewById(R.id.exhibition_detail_like_clickable);
        // ImageViewオブジェクトにクリックイベントを追加する
        if(like != null) {
            like.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View v) {

                            if(item.getLiked() == null) return;

                            String url = "https://api.hokutosai.tech/2016/exhibitions/" + item.getExhibition_id() + "/likes";
                            int method = Request.Method.POST;
                            if(item.getLiked()) method = Request.Method.DELETE; //いいね済みの状態でいいねを押した場合はDELETE

                            MyStringRequest postJson = new MyStringRequest(method, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // TODO 自動生成されたメソッド・スタブ
                                            if( activity != null) {
                                                like.setSelected(!like.isSelected());   //画像の反転

                                                TextView like_count = (TextView)findViewById(R.id.exhibition_detail_like_count);

                                                if( like.isSelected() ){
                                                    Toast.makeText(activity, "いいねしました", Toast.LENGTH_SHORT).show();
                                                    ++mLikeCount;
                                                    like_count.setText("いいね：" + String.valueOf(mLikeCount) + "件");
                                                }
                                                else{
                                                    Toast.makeText(activity, "いいねを取り消しました", Toast.LENGTH_SHORT).show();
                                                    --mLikeCount;
                                                    like_count.setText("いいね：" + String.valueOf(mLikeCount) + "件");
                                                }
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            if(activity != null) Toast.makeText(activity, "いいねに失敗しました", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                            );

                            postJson.setCustomTimeOut();   //タイムアウト時間の変更
                            postJson.setTag(TAG_EXHIBITION_LIKE_REQUEST_QUEUE);    //タグのセット
                            RequestQueueSingleton.getInstance().add(postJson);    //WebAPIの呼び出し
                        }
                    }
            );
        }
    }

    public void clickMapResult(View view){

        if( !mExhibitionDetail.title.isEmpty() ) {
            Resources r = getResources();
            Bitmap bmp = BitmapFactory.decodeResource(r, R.mipmap.layout_map);
            MyApplication.getInstance().setBmp(bmp);
            Intent i = new Intent(ExhibitionDetailActivity.this, ImageActivity.class);
            startActivity(i);
        }
    }

    public void reviewShowClickResult( View view ){

        if( !mExhibitionDetail.title.isEmpty() ) {
            Intent i = new Intent(ExhibitionDetailActivity.this, ReviewListActivity.class);
            ReviewAssessment ra = new ReviewAssessment();
            ra.setName(mExhibitionDetail.title);
            ra.setId(mExhibitionDetail.exhibition_id);
            ra.setMy_assessment(mExhibitionDetail.my_assessment);
            ra.setAssessments(mExhibitionDetail.assessments);
            ra.setAssessment_aggregate(mExhibitionDetail.assessment_aggregate);
            ra.setType(ReviewAssessment.Type.EXHIBITION);
            i.putExtra("ReviewAssessment", ra);
            startActivity(i);
        }
    }

    public void reviewWriteClickResult( View view ){

        if( !mExhibitionDetail.title.isEmpty() ) {
            LayoutInflater factory = LayoutInflater.from(this);
            final View inputView = factory.inflate(R.layout.dialog_shop_review, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("展示の評価");
            builder.setView(inputView);

            EditText user = (EditText)inputView.findViewById(R.id.dialog_user_name);
            EditText comment = (EditText)inputView.findViewById(R.id.dialog_comment);
            RatingBar rate = (RatingBar)inputView.findViewById(R.id.dialog_rate);

            if(mExhibitionDetail.my_assessment != null) {
                if( mExhibitionDetail.my_assessment.getAccount().getUser_name() != null && !mExhibitionDetail.my_assessment.getAccount().getUser_name().isEmpty() ) {
                    user.setText(mExhibitionDetail.my_assessment.getAccount().getUser_name());
                }
                if( mExhibitionDetail.my_assessment.getComment() != null && !mExhibitionDetail.my_assessment.getComment().isEmpty() ){
                    comment.setText(mExhibitionDetail.my_assessment.getComment());
                }
                rate.setRating( mExhibitionDetail.my_assessment.getScore());
            }

            builder.setPositiveButton("送信", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int whichButton) {

                    final EditText user = (EditText)inputView.findViewById(R.id.dialog_user_name);
                    final EditText comment = (EditText)inputView.findViewById(R.id.dialog_comment);
                    final RatingBar rate = (RatingBar)inputView.findViewById(R.id.dialog_rate);


                    if(comment.getText().toString().isEmpty() || comment.getText().toString().equals(System.getProperty("line.separator")) ){
                        Toast.makeText(ExhibitionDetailActivity.this, "コメントが入力されていません", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if( rate.getRating() == 0 ){
                        Toast.makeText(ExhibitionDetailActivity.this, "星の数を決めてください", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //コメント送信処理
                    String url = "https://api.hokutosai.tech/2016/exhibitions/" + mExhibitionDetail.exhibition_id + "/assessment";
                    // 送信したいパラメーター
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("score", String.valueOf((int)rate.getRating()) );
                    params.put("comment", comment.getText().toString());
                    params.put("user_name", user.getText().toString());
                    int method = Request.Method.POST;

                    MyStringRequest postJson = new MyStringRequest(method, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    // TODO 自動生成されたメソッド・スタブ
                                    if( ExhibitionDetailActivity.this != null) {

                                        Gson gson = new Gson();
                                        ExhibitionMyAssessment exhibitionMyAssessment = gson.fromJson(response.toString(), ExhibitionMyAssessment.class);
                                        //データの更新
                                        Iterator<Assessment> i = mExhibitionDetail.assessments.iterator();
                                        while(i.hasNext()){ //リストから自分のコメントがあればいったん削除
                                            Assessment as = i.next();
                                            if(as.getAccount().account_id.equals(exhibitionMyAssessment.my_assessment.getAccount().account_id)){
                                                i.remove();
                                                break;
                                            }
                                        }
                                        mExhibitionDetail.assessment_aggregate = exhibitionMyAssessment.assessment_aggregate;
                                        mExhibitionDetail.my_assessment = exhibitionMyAssessment.my_assessment;

                                        mExhibitionDetail.my_assessment.getAccount().setUser_name(user.getText().toString());
                                        exhibitionMyAssessment.my_assessment.getAccount().setUser_name(user.getText().toString());

                                        mExhibitionDetail.exhibition_id = exhibitionMyAssessment.exhibition_id;
                                        mExhibitionDetail.assessments.add(0,exhibitionMyAssessment.my_assessment);    //自分のコメントをリストの先頭に追加

                                        Toast.makeText(ExhibitionDetailActivity.this, "評価しました", Toast.LENGTH_SHORT).show();

                                        TextView deleteText = (TextView)ExhibitionDetailActivity.this.findViewById(R.id.exhibition_detail_delete_review);
                                        deleteText.setClickable(true);  //レビュー削除ボタンを有効に
                                        deleteText.setTextColor(getResources().getColor(R.color.text_clickable));
                                        TextView reviewText = (TextView)ExhibitionDetailActivity.this.findViewById(R.id.exhibition_detail_write_review);
                                        reviewText.setText("自分のレビューを修正する");

                                        //評価(再描画)******************************************************************************************
                                        float rate = mExhibitionDetail.assessment_aggregate.getTotal_score() / (float) mExhibitionDetail.assessment_aggregate.getAssessed_count();
                                        if(Float.isNaN(rate)) rate = 0;
                                        RatingBar allRate = (RatingBar)findViewById(R.id.exhibition_detail_all_rate);
                                        allRate.setRating( rate );
                                        TextView allRateString = (TextView)findViewById(R.id.exhibition_detail_all_rate_str);
                                        allRateString.setText( "(" + String.format("%.2f", rate) + ")" );
                                        TextView allRateNum = (TextView)findViewById(R.id.exhibition_detail_all_rate_num);
                                        allRateNum.setText("評価件数：" + mExhibitionDetail.assessment_aggregate.getAssessed_count());
                                        //******************************************************************************************************
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("test",error.toString());
                                    if(ExhibitionDetailActivity.this != null) Toast.makeText(ExhibitionDetailActivity.this, "評価に失敗しました", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );

                    postJson.setCustomTimeOut();                          //タイムアウト時間の変更
                    postJson.setTag(TAG_EXHIBITION_LIKE_REQUEST_QUEUE);   //タグのセット
                    postJson.setParams(params);                           //パラメータのセット
                    RequestQueueSingleton.getInstance().add(postJson);    //WebAPIの呼び出し

                }
            });

            builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void reviewDeleteClickResult( View view ) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ExhibitionDetailActivity.this);
        builder.setTitle("レビューの削除").setMessage("自分の書いたレビューを削除しますか")
                .setPositiveButton("はい",
                        new DialogInterface.OnClickListener() {

                            //はいの時の処理
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //コメント送信処理
                                String url = "https://api.hokutosai.tech/2016/exhibitions/" + String.valueOf(mExhibitionDetail.exhibition_id) + "/assessment";
                                Log.d("test",url);
                                int method = Request.Method.DELETE;

                                MyStringRequest postJson = new MyStringRequest(method, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                // TODO 自動生成されたメソッド・スタブ
                                                Log.d("test",response.toString());
                                                if( ExhibitionDetailActivity.this != null) {

                                                    Gson gson = new Gson();
                                                    ExhibitionMyAssessment exhibitionMyAssessment = gson.fromJson(response.toString(), ExhibitionMyAssessment.class);
                                                    //データの更新
                                                    Iterator<Assessment> i = mExhibitionDetail.assessments.iterator();
                                                    while(i.hasNext()){ //リストから自分のコメントを削除
                                                        Assessment as = i.next();
                                                        if(as.getAccount().account_id.equals(mExhibitionDetail.my_assessment.getAccount().account_id)){
                                                            i.remove();
                                                            break;
                                                        }
                                                    }
                                                    mExhibitionDetail.assessment_aggregate = exhibitionMyAssessment.assessment_aggregate;
                                                    mExhibitionDetail.my_assessment = exhibitionMyAssessment.my_assessment;
                                                    mExhibitionDetail.exhibition_id = exhibitionMyAssessment.exhibition_id;

                                                    Toast.makeText(ExhibitionDetailActivity.this, "レビューを削除しました", Toast.LENGTH_SHORT).show();

                                                    TextView deleteText = (TextView)ExhibitionDetailActivity.this.findViewById(R.id.exhibition_detail_delete_review);
                                                    deleteText.setClickable(false);  //レビュー削除ボタンを無効に
                                                    deleteText.setTextColor(getResources().getColor(R.color.ClickdisableText));
                                                    TextView reviewText = (TextView)ExhibitionDetailActivity.this.findViewById(R.id.exhibition_detail_write_review);
                                                    reviewText.setText("レビューを書く");

                                                    //評価(再描画)******************************************************************************************
                                                    float rate = mExhibitionDetail.assessment_aggregate.getTotal_score() / (float) mExhibitionDetail.assessment_aggregate.getAssessed_count();
                                                    if(Float.isNaN(rate)) rate = 0;
                                                    RatingBar allRate = (RatingBar)findViewById(R.id.exhibition_detail_all_rate);
                                                    allRate.setRating( rate );
                                                    TextView allRateString = (TextView)findViewById(R.id.exhibition_detail_all_rate_str);
                                                    allRateString.setText( "(" + String.format("%.2f", rate) + ")" );
                                                    TextView allRateNum = (TextView)findViewById(R.id.exhibition_detail_all_rate_num);
                                                    allRateNum.setText("評価件数：" + mExhibitionDetail.assessment_aggregate.getAssessed_count());
                                                    //******************************************************************************************************
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d("test",String.valueOf(error.networkResponse.statusCode));
                                                if(ExhibitionDetailActivity.this != null) Toast.makeText(ExhibitionDetailActivity.this, "削除に失敗しました", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                );
                                postJson.setCustomTimeOut();                          //タイムアウト時間の変更
                                postJson.setTag(TAG_EXHIBITION_LIKE_REQUEST_QUEUE);         //タグのセット
                                RequestQueueSingleton.getInstance().add(postJson);    //WebAPIの呼び出し

                            }
                        }
                )
                .setNeutralButton("キャンセル",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO 自動生成されたメソッド・スタブ

                            }
                        }
                ).show();
    }

    private class ExhibitionDetail{
        int exhibition_id;
        String title;
        String exhibitors;
        String displays;
        String image_url;
        AssessedScore assessment_aggregate;
        Boolean liked;
        int likes_count;
        String introduction;
        Place place;
        List<Assessment> assessments;
        Assessment my_assessment;
    }

    private class ExhibitionMyAssessment{
        int exhibition_id;
        Assessment my_assessment;
        AssessedScore assessment_aggregate;
    }
}
