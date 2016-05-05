package com.hokutosai.hokutosai_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ryoji on 2016/05/05.
 */
public class EventDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_detail);

        Intent i = getIntent();
        final Event item = (Event)i.getSerializableExtra("Event");
    }
}
