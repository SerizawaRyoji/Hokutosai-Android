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
    ArrayList<ExhibitionItem> exhibitionItemList;
    private ViewHolder holder = null;

    public ExhibitionItemAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setExhibitionItemList(ArrayList<ExhibitionItem> itemList) {
        this.exhibitionItemList = itemList;
    }

    @Override
    public int getCount() {
        return exhibitionItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return exhibitionItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return exhibitionItemList.get(position).getExhibition_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.exhibition_item, null);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.exhibition_item_title);
            holder.display = (TextView) convertView.findViewById(R.id.exhibition_item_displays);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.title.setText(exhibitionItemList.get(position).getTitle());
        holder.display.setText(exhibitionItemList.get(position).getDisplays());

        if (exhibitionItemList.get(position).getImage_url() != null) {
            NetworkImageView imageView = (NetworkImageView) convertView.findViewById(R.id.exhibition_item_image);
            imageView.setImageUrl(exhibitionItemList.get(position).getImage_url(), ImageLoaderSingleton.getImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()) );
        }

        return convertView;
    }

    private class ViewHolder{
        TextView title;
        TextView display;
    }
}
