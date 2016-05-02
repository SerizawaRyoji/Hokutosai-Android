package com.hokutosai.hokutosai_android;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by ryoji on 2016/05/02.
 */
public class LruCacheSingleton implements ImageLoader.ImageCache {

    private static LruCacheSingleton instance = null;
    private LruCache<String, Bitmap> mMemoryCache;

    LruCacheSingleton(){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;       // 最大メモリに依存
        // int cacheSize = 5 * 1024 * 1024;  // 5MB

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 使用キャッシュサイズ(KB単位)
                return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024;
            }
        };
    }

    public static void create(){
        if(instance == null){
            instance = new LruCacheSingleton();
        }
    }

    public static LruCacheSingleton getInstance(){
        return instance;
    }

    // ImageCacheのインターフェイス実装
    @Override
    public Bitmap getBitmap(String url) {
        return mMemoryCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mMemoryCache.put(url,bitmap);
    }

    //キャッシュの初期化（リスト選択終了時に呼び出し、キャッシュで使用していたメモリを解放する）
  /* public static void clearCache(){
   	mMemoryCache = null;
   	mMemoryCache = new mMemoryCache<String,Bitmap>();
   }*/

}
