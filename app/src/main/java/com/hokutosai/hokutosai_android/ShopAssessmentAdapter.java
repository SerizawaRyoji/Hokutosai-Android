package com.hokutosai.hokutosai_android;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ryoji on 2016/05/15.
 */
public class ShopAssessmentAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Assessment> asList;
    private ViewHolder holder = null;
    private ArrayList<ShopReviewActivity.AssessmentReportCause> reportList;
    private ArrayList<String> reportStringList;

    public ShopAssessmentAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setAssessmentList(ArrayList<Assessment> itemList) {
        this.asList = itemList;
    }

    public void setAssessmentReportCauseList(ArrayList<ShopReviewActivity.AssessmentReportCause> itemList){
        reportList = itemList;
        reportStringList = new ArrayList<>();
        for(int i=0 ; i<reportList.size() ; ++i) {
            reportStringList.add(reportList.get(i).text);
        }
    }

    @Override
    public int getCount() {
        return asList.size();
    }

    @Override
    public Object getItem(int position) {
        return asList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return asList.get(position).getAssessment_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.assessment_item, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.assessment_name);
            holder.rate = (RatingBar) convertView.findViewById(R.id.assessment_rate);
            holder.datetime = (TextView) convertView.findViewById(R.id.assessment_datetime);
            holder.comment = (TextView) convertView.findViewById(R.id.assessment_comment);
            holder.report = (TextView) convertView.findViewById(R.id.assessment_report);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        if( asList.get(position).getAccount() == null || asList.get(position).getAccount().getUser_name() == null || asList.get(position).getAccount().getUser_name().isEmpty()) {
                holder.name.setText("ゲスト");
        }
        else{
            holder.name.setText(asList.get(position).getAccount().getUser_name());
        }

        holder.rate.setRating(asList.get(position).getScore());
        holder.datetime.setText( MyDateFormatSingleton.getInstance().getDateTime(asList.get(position).getDatetime()) );
        holder.comment.setText(asList.get(position).getComment());

        final Context context = parent.getContext();
        final int pos = position;
        holder.report.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("通報理由の選択");

                final Spinner spinner = new Spinner(context);
                ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, reportStringList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                builder.setView(spinner);

                builder.setPositiveButton("送信", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {

                        int idx = spinner.getSelectedItemPosition();
                        callAssessmentReport(pos, idx, context);                    //通報の送信処理
                    }
                });
                builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return convertView;
    }

    private void callAssessmentReport(int position, int reportSelectedPosition, final Context context ){

        //コメント送信処理
        String url = "https://api.hokutosai.tech/2016/shops/assessment/" + asList.get(position).assessment_id + "/report";
        // 送信したいパラメーター
        Map<String, String> params = new HashMap<String, String>();
        params.put("cause", reportList.get(reportSelectedPosition).cause_id );
        int method = Request.Method.POST;

        MyStringRequest postJson = new MyStringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(context != null) Toast.makeText(context, "通報しました", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(context != null) Toast.makeText(context, "通報に失敗しました", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        postJson.setCustomTimeOut();                          //タイムアウト時間の変更
        postJson.setParams(params);                           //パラメータのセット
        RequestQueueSingleton.getInstance().add(postJson);    //WebAPIの呼び出し
    }

    private class ViewHolder{
        TextView name;
        RatingBar rate;
        TextView datetime;
        TextView comment;
        TextView report;
    }
}
