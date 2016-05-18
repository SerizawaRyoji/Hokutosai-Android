package com.hokutosai.hokutosai_android;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ryoji on 2016/05/15.
 */
public class ShopAssessmentAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Assessment> asList;
    private ViewHolder holder = null;
    private ArrayList<ShopReviewActivity.AssessmentReportCause> reportList;

    public ShopAssessmentAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setAssessmentList(ArrayList<Assessment> itemList) {
        this.asList = itemList;
    }

    public void setAssessmentReportCauseList(ArrayList<ShopReviewActivity.AssessmentReportCause> itemList){reportList = itemList; }

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
        holder.report.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("模擬店の評価");

                builder.setPositiveButton("送信", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
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

    private class ViewHolder{
        TextView name;
        RatingBar rate;
        TextView datetime;
        TextView comment;
        TextView report;
    }
}
