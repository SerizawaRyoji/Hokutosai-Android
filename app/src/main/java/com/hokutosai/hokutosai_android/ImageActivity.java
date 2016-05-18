package com.hokutosai.hokutosai_android;

import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by ryoji on 2016/05/17.
 */
public class ImageActivity extends AppCompatActivity {

    ImageView mImageView;
    PhotoViewAttacher mAttacher;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("画像");

        setContentView(R.layout.activity_image);

        bmp = MyApplication.getInstance().getBmp();

        mImageView = (ImageView)findViewById(R.id.image_activity);
        mImageView.setImageBitmap(bmp);

        mAttacher = new PhotoViewAttacher(mImageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.image_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){ // if使うとエラー（itemがInt形式なため）
            case android.R.id.home:   // アプリアイコン（ホームアイコン）を押した時の処理
                finish();
                break;

            case R.id.menu_download:    //ダウンロードボタン

                try {
                    // sdcardフォルダを指定
                    File file = new File(Environment.getExternalStorageDirectory() + "/Hokutosai-Pictures/");
                    Log.d("test",file.getPath());
                    try{
                        if(!file.exists()){
                            file.mkdir();
                        }
                    }catch(SecurityException e){
                        e.printStackTrace();
                        throw e;
                    }

                    //日付でファイル名を作成
                    Date mDate = new Date();
                    SimpleDateFormat fileNameDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String fileName = fileNameDate.format(mDate) + ".jpg";
                    String AttachName = file.getAbsolutePath() + "/" + fileName;

                    try {
                        FileOutputStream out = new FileOutputStream(AttachName);
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch(IOException e) {
                        e.printStackTrace();
                        throw e;
                    }

                    //最後のContentResolverの部分で保存したファイルパスを登録   *これをしないとデバイスを再起動するまでギャラリーに表示されない
                    /*ContentValues values = new ContentValues();
                    ContentResolver contentResolver = getContentResolver();
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    values.put("_data", AttachName);
                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);*/

                    File f = new File(Environment.getExternalStorageDirectory() + "/Hokutosai-Pictures/",fileName);

                    // MediaScannerConnection を使用する場合
                    MediaScannerConnection.scanFile( // API Level 8
                            this, // Context
                            new String[] { f.getPath() },
                            new String[] { "image/jpeg" },
                            null);

                    Toast.makeText(ImageActivity.this,file.getPath() + "に画像を保存しました",Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e("Error", "" + e.toString());
                    Toast.makeText(ImageActivity.this,"画像の保存に失敗しました",Toast.LENGTH_SHORT).show();
                }

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
