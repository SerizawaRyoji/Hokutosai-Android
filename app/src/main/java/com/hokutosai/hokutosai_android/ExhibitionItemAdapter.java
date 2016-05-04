package com.hokutosai.hokutosai_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by ryoji on 2016/05/03.
 */
public class ExhibitionItemAdapter  extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Exhibition> exhibitionList;
    private ViewHolder holder = null;

    public ExhibitionItemAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setExhibitionList(ArrayList<Exhibition> itemList) {
        this.exhibitionList = itemList;
    }

    @Override
    public int getCount() {
        return exhibitionList.size();
    }

    @Override
    public Object getItem(int position) {
        return exhibitionList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return exhibitionList.get(position).getExhibition_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.exhibition_item, null);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.exhibition_item_title);
            holder.display = (TextView) convertView.findViewById(R.id.exhibition_item_displays);
            holder.image = (NetworkImageView) convertView.findViewById(R.id.exhibition_item_image);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.title.setText(exhibitionList.get(position).getTitle());
        holder.display.setText(exhibitionList.get(position).getDisplays());

        if (exhibitionList.get(position).getImage_url() != null) {
            holder.image.setImageUrl(exhibitionList.get(position).getImage_url(), ImageLoaderSingleton.getImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()) );
        }

        return convertView;
    }

    private class ViewHolder{
        TextView title;
        TextView display;
        NetworkImageView image;
    }
}
