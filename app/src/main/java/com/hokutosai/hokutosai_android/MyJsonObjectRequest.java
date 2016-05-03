package com.hokutosai.hokutosai_android;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ryoji on 2016/05/03.
 */
public class MyJsonObjectRequest extends JsonObjectRequest {

    private static final int CUSTOM_TIMEOUT_MS = 1000;

    public MyJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener,
                               Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        // TODO 自動生成されたコンストラクター・スタブ
    }

    //WebAPIの認証
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        // Add BASIC AUTH HEADER
        Map<String, String> newHeaders = new HashMap<String, String>();
        newHeaders.putAll(headers);
        newHeaders.put("Authorization", "user_id=client-android-app,access_token=fIsngZeqTRUOjl8HtlqRnhjPK8TTaDnd3bFsgda8fxMVpBGX180Ld3Hlr5gT30tr");
        return newHeaders;
    }

    //volleyのタイムアウト時間の変更 *変更しないとタイムアウトによるエラーが発生したため
    //http://qiita.com/ya13241ba/items/c6a5ebb93afbae3d039a
    public void setCustomTimeOut(){
        DefaultRetryPolicy policy = new DefaultRetryPolicy(CUSTOM_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        this.setRetryPolicy(policy);
    }
}
