package com.hokutosai.hokutosai_android;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by ryoji on 2016/05/02.
 */
public class ImageLoaderSingleton {

    private static MyImageLoader sImageLoader;

    public static MyImageLoader getImageLoader(RequestQueue queue, ImageLoader.ImageCache bitmapCache) {
        if (sImageLoader == null) {
            sImageLoader = new MyImageLoader(queue, bitmapCache);
        }
        return sImageLoader;
    }
}
