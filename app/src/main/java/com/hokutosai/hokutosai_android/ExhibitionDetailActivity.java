package com.hokutosai.hokutosai_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

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

        TextView name = (TextView)this.findViewById(R.id.exhibition_detail_name);
        TextView exhibitors = (TextView)this.findViewById(R.id.exhibition_detail_exhibitors);
        TextView displays = (TextView)this.findViewById(R.id.exhibition_detail_displays);

        name.setText(item.getTitle());
        exhibitors.setText(item.getExhibitors());
        displays.setText(item.getDisplays());

        String url = "https://api.hokutosai.tech/2016/exhibitions/";
        url += String.valueOf(item.getExhibition_id());
        url += "/details";
    }

    private class ExhibitionDetail{
        int exhibition_id;
        String title;
        String exhibitors;
        String displays;
        String image_url;
        AssessedScore assessed_score;
        Boolean liked;
        String introduction;
        PlaceItem place;
        List<Assessment> assessments;
        Assessment my_assessment;
    }
}
