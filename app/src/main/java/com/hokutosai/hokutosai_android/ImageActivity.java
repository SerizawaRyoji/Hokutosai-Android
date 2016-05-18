package com.hokutosai.hokutosai_android;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
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

        Bitmap bmp = MyApplication.getInstance().getBmp();

        mImageView = (ImageView)findViewById(R.id.image_activity);
        mImageView.setImageBitmap(bmp);

        mAttacher = new PhotoViewAttacher(mImageView);
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
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getInstance().clearBmp();
    }
}
