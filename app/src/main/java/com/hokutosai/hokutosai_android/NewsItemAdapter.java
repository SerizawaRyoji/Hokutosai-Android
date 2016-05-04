package com.hokutosai.hokutosai_android;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ryoji on 2016/05/05.
 */
public class NewsItemAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<News> newsItemList;
    private ViewHolder holder = null;

    public NewsItemAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setNewsItemList(ArrayList<News> itemList) {
        this.newsItemList = itemList;
    }

    @Override
    public int getCount() {
        return newsItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return newsItemList.get(position).getNews_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.news_item, null);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.news_item_title);
            holder.related = (TextView) convertView.findViewById(R.id.news_item_related);
            holder.datetime = (TextView) convertView.findViewById(R.id.news_item_datetime);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.title.setText(newsItemList.get(position).getTitle());
        holder.related.setText("related");        //修正必要あり   newsItemList.get(position).getRelated_event().title
        holder.datetime.setText(newsItemList.get(position).getDatetime());
        Log.d("test",newsItemList.get(position).getTitle() + ":" + newsItemList.get(position).getDatetime());
        if ( newsItemList.get(position).getMedias() != null ) {
            //NetworkImageView imageView = (NetworkImageView) convertView.findViewById(R.id.news_item_image);
            //imageView.setImageUrl(newsItemList.get(position).getMedias().get(0).url, ImageLoaderSingleton.getImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()) );
        }

        return convertView;
    }

    private class ViewHolder{
        TextView title;
        TextView related;
        TextView datetime;
    }
}