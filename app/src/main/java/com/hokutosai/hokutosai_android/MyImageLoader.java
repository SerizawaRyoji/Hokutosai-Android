package com.hokutosai.hokutosai_android;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by ryoji on 2016/05/02.
 */
public class MyImageLoader  extends ImageLoader {

    public MyImageLoader(RequestQueue queue, ImageCache imageCache) {
        super(queue, imageCache);
    }


    public static ImageListener getImageListener(
            final ImageView view, final int defaultImageResId, final int errorImageResId) {
        return new ImageListener() {
            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {

                if (response.getBitmap() != null) {
                    view.setImageBitmap(response.getBitmap());
                } else if (defaultImageResId != 0) {
                    view.setImageResource(defaultImageResId);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageResId != 0) {
                    view.setImageResource(errorImageResId);
                }
            }
        };
    }

    public static ImageListener getImageListener(
            final ImageView view, final ProgressBar progressBar, final int errorImageResId) {
        return new ImageListener() {
            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {

                if (response.getBitmap() != null) {
                    view.setImageBitmap(response.getBitmap());
                } else {
                    view.setImageResource(errorImageResId);
                }

                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                if (errorImageResId != 0) {
                    view.setImageResource(errorImageResId);
                } else {
                    view.setImageResource(android.R.drawable.ic_menu_report_image);
                }

                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        };
    }
}
