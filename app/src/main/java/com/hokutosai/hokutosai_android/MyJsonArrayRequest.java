package com.hokutosai.hokutosai_android;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

    private static final int CUSTOM_TIMEOUT_MS = 10000;

    public MyJsonArrayRequest(String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        // TODO 自動生成されたコンストラクター・スタブ
    }

    //WebAPIの認証
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        String header = MyAccountSingleton.getInstance().getAccountHeader();

        // Add BASIC AUTH HEADER
        Map<String, String> newHeaders = new HashMap<String, String>();
        newHeaders.putAll(headers);
        newHeaders.put("Authorization", header);

        return newHeaders;
    }

    //volleyのタイムアウト時間の変更 *変更しないとタイムアウトによるエラーが発生したため
    //http://qiita.com/ya13241ba/items/c6a5ebb93afbae3d039a
    public void setCustomTimeOut(){
        DefaultRetryPolicy policy = new DefaultRetryPolicy(CUSTOM_TIMEOUT_MS,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        this.setRetryPolicy(policy);
    }
}
