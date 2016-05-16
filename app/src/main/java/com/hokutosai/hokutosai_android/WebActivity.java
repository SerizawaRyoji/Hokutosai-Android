package com.hokutosai.hokutosai_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.webkit.WebView;

/**
 * Created by ryoji on 2016/05/16.
 */
public class WebActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO 自動生成されたメソッド・スタブ
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Webページ");

        setContentView(R.layout.activity_web);

        Intent i = getIntent();
        String url = (String)i.getStringExtra("URL");

        WebView wv = (WebView)this.findViewById(R.id.web_view);

        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){ // if使うとエラー（itemがInt形式なため）
            case android.R.id.home:   // アプリアイコン（ホームアイコン）を押した時の処理
				/*Intent intent = new Intent(this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);*/
                finish();
                break;
        }
        return true;
    }
}
