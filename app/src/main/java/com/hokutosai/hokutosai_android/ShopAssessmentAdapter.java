package com.hokutosai.hokutosai_android;

import android.content.Context;
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

    public ShopAssessmentAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setAssessmentList(ArrayList<Assessment> itemList) {
        this.asList = itemList;
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

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        if( asList.get(position).getAccount().getUser_name().isEmpty() ) holder.name.setText("ゲスト");
        else holder.name.setText(asList.get(position).getAccount().getUser_name());
        holder.rate.setRating(asList.get(position).getScore());
        holder.datetime.setText( MyDateFormatSingleton.getInstance().getDateTime(asList.get(position).getDatetime()) );
        holder.comment.setText(asList.get(position).getComment());

        return convertView;
    }

    private class ViewHolder{
        TextView name;
        RatingBar rate;
        TextView datetime;
        TextView comment;
    }
}
