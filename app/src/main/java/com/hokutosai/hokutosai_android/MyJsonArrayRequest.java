package com.hokutosai.hokutosai_android;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ryoji on 2016/05/02.
 */
public class MyJsonArrayRequest extends JsonArrayRequest
{

    public MyJsonArrayRequest(String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
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
}
