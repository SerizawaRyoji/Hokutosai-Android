package com.hokutosai.hokutosai_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ryoji on 2016/05/15.
 */
public class ShopReviewActivity extends AppCompatActivity {

    ShopAssessmentAdapter adapter;
    ListView listView = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shop_review);

        Intent i = getIntent();
        final ShopDetailActivity.ShopDetail item = (ShopDetailActivity.ShopDetail)i.getSerializableExtra("ShopDetail");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(item.name);


        //評価***************************************************************************************************
        float rate = item.assessment_aggregate.getTotal_score() / (float) item.assessment_aggregate.getAssessed_count();
        RatingBar allRate = (RatingBar)findViewById(R.id.shop_review_all_rate);
        allRate.setRating( rate );
        TextView allRateString = (TextView)findViewById(R.id.shop_review_all_rate_str);
        allRateString.setText( "(" + String.format("%.2f", rate) + ")" );
        TextView allRateNum = (TextView)findViewById(R.id.shop_review_all_rate_num);
        allRateNum.setText("評価件数：" + item.assessment_aggregate.getAssessed_count());
        //******************************************************************************************************

        adapter = new ShopAssessmentAdapter( this );

        adapter.setAssessmentList( new ArrayList<>(item.assessments) );
        listView = (ListView) findViewById(R.id.list_shop_review);
        listView.setAdapter(adapter);
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
}
