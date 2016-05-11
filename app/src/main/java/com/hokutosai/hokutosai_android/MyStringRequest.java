package com.hokutosai.hokutosai_android;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ryoji on 2016/05/11.
 */
public class MyStringRequest extends StringRequest {

    Map<String, String> mParams;

    public MyStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        // TODO 自動生成されたコンストラクター・スタブ
    }

    //WebAPIの認証
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();

        String header = MyAccountSingleton.getInstance().getAccountHeader();

        Map<String, String> newHeaders = new HashMap<String, String>();
        newHeaders.putAll(headers);
        newHeaders.put("Authorization", header);
        return newHeaders;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    public void setParams(Map<String, String> params){
        this.mParams = params;
    }

    public void setCustomTimeOut(){
        DefaultRetryPolicy policy = new DefaultRetryPolicy(500,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        this.setRetryPolicy(policy);
    }
}
