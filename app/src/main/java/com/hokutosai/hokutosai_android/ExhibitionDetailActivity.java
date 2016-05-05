package com.hokutosai.hokutosai_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ryoji on 2016/05/05.
 */
public class ExhibitionDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exhibition_detail);

        Intent i = getIntent();
        final Exhibition item = (Exhibition)i.getSerializableExtra("Exhibition");
    }
}
