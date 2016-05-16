package com.hokutosai.hokutosai_android;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by ryoji on 2016/05/17.
 */
public class ImageActivity extends AppCompatActivity {

    ImageView mImageView;
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("画像");

        setContentView(R.layout.activity_image);

        Resources r = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(r, R.mipmap.layout_map);

        mImageView = (ImageView)findViewById(R.id.image_activity);
        mImageView.setImageBitmap(bmp);

        mAttacher = new PhotoViewAttacher(mImageView);
    }
}
