package com.hokutosai.hokutosai_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ryoji on 2016/05/15.
 */
public class ReviewListActivity extends AppCompatActivity {

    ShopAssessmentAdapter adapter;
    ListView listView = null;
    ArrayList<AssessmentReportCause> reportList = new ArrayList<>();

    //Volleyでリクエスト時に設定するタグ名。キャンセル時に利用する。
    private static final Object TAG_ASSESSMENT_REPORTS_REQUEST_QUEUE = new Object();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shop_review);

        Intent i = getIntent();
        final ShopDetailActivity.ShopDetail item = (ShopDetailActivity.ShopDetail)i.getSerializableExtra("ShopDetail");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(item.name);

        MyJsonArrayRequest jArrayRequest =
                new MyJsonArrayRequest("https://api.hokutosai.tech/2016/assessments/reports/causes",
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                                //JSONArrayをListShopItemに変換して取得
                                Gson gson = new Gson();
                                Type collectionType = new TypeToken<Collection<AssessmentReportCause>>() {
                                }.getType();
                                Log.d("test",response.toString());

                                if(ReviewListActivity.this != null) {
                                    reportList = gson.fromJson(response.toString(), collectionType);      //通報理由の一覧を保持

                                    //評価***************************************************************************************************
                                    float rate = item.assessment_aggregate.getTotal_score() / (float) item.assessment_aggregate.getAssessed_count();
                                    if (Float.isNaN(rate)) rate = 0;
                                    RatingBar allRate = (RatingBar) findViewById(R.id.shop_review_all_rate);
                                    allRate.setRating(rate);
                                    TextView allRateString = (TextView) findViewById(R.id.shop_review_all_rate_str);
                                    allRateString.setText("(" + String.format("%.2f", rate) + ")");
                                    TextView allRateNum = (TextView) findViewById(R.id.shop_review_all_rate_num);
                                    allRateNum.setText("評価件数：" + item.assessment_aggregate.getAssessed_count());
                                    //******************************************************************************************************

                                    adapter = new ShopAssessmentAdapter(ReviewListActivity.this);

                                    adapter.setAssessmentList(new ArrayList<>(item.assessments));
                                    adapter.setAssessmentReportCauseList(reportList);

                                    listView = (ListView) findViewById(R.id.list_shop_review);
                                    listView.setAdapter(adapter);

                                    listView.setEnabled(false);
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
        jArrayRequest.setTag(TAG_ASSESSMENT_REPORTS_REQUEST_QUEUE);    //タグのセット
        RequestQueueSingleton.getInstance().add(jArrayRequest);    //WebAPIの呼び出し

    }

    @Override
    public void onStop() {
        super.onStop();
        RequestQueueSingleton.getInstance().cancelAll(TAG_ASSESSMENT_REPORTS_REQUEST_QUEUE);
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

    public class AssessmentReportCause {
        String cause_id;
        String text;
    }
}
