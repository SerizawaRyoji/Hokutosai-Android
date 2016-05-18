package com.hokutosai.hokutosai_android;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by ryoji on 2016/05/08.
 */
public class MyAccountSingleton {

    private static MyAccountSingleton mAccount = new MyAccountSingleton();
    private AccountCredentials mAccountCredentials = new AccountCredentials();
    private Boolean isAccountCreated = false;

    static final String HEADER = "user_id=client-android-app,access_token=fIsngZeqTRUOjl8HtlqRnhjPK8TTaDnd3bFsgda8fxMVpBGX180Ld3Hlr5gT30tr";

    public static MyAccountSingleton getInstance() { return mAccount; }

    public void createAccount(Application app){

        isAccountCreated = false;

        //アカウントデータの取得
        SharedPreferences data = app.getSharedPreferences("DataSave", Context.MODE_PRIVATE);
        mAccountCredentials.account_id = data.getString("account_id", "" );
        mAccountCredentials.account_pass = data.getString("account_pass", "" );
        Log.d("test","check");

        if( mAccount.mAccountCredentials.account_id.isEmpty() || mAccount.mAccountCredentials.account_pass.isEmpty() ){   //アカウントが存在しないなら

            String url = "https://api.hokutosai.tech/2016/accounts/new";

            MyJsonObjectRequest jObjectRequest =
                    new MyJsonObjectRequest(Request.Method.GET,url,null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    //JSONArrayをListShopItemに変換して取得
                                    Gson gson = new Gson();
                                    mAccountCredentials = gson.fromJson(response.toString(), AccountCredentials.class); //アカウントの作成

                                    SharedPreferences data = MyApplication.getInstance().getSharedPreferences("DataSave", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = data.edit();
                                    editor.putString("account_id", mAccountCredentials.account_id);
                                    editor.putString("account_pass", mAccountCredentials.account_pass);
                                    editor.apply();

                                    Log.d("test","create new account");
                                    isAccountCreated = true;
                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.e("LIFE", error.toString());
                                    // エラー処理 error.networkResponseで確認
                                    // エラー表示など
                                }
                            });

            jObjectRequest.setCustomTimeOut();   //タイムアウト時間の変更
            RequestQueueSingleton.getInstance().add(jObjectRequest);    //WebAPIの呼び出し
        }
        else{   //アカウントが存在したなら
            Log.d("test","account id already created");
            isAccountCreated = true;
        }
    }

    public String getAccountHeader(){

        if(mAccountCredentials.account_pass.isEmpty() || mAccountCredentials.account_id.isEmpty()){
            return MyAccountSingleton.HEADER;
        }

        return HEADER + ",account_id=" + mAccount.mAccountCredentials.account_id + ",account_pass=" + mAccount.mAccountCredentials.account_pass;
    }

    public Boolean getIsAccountCreated(){
        return isAccountCreated;
    }

    private class AccountCredentials{
        String account_id;
        String account_pass;
    }
}
