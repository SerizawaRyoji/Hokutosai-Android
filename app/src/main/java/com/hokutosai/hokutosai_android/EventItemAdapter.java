package com.hokutosai.hokutosai_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * Created by ryoji on 2016/05/04.
 */
public class EventItemAdapter extends ArrayAdapter<Event> {

    private LayoutInflater mInflater;
    private int resource = 0;

    public EventItemAdapter(Context context, List<Event> data, int resource) {
        super(context, 0, data);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;	//リソースを保持しておく
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {	//取得すべきビューがnullなら
            convertView = mInflater.inflate(this.resource, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView)convertView.findViewById(R.id.event_item_title);
            holder.date = (TextView)convertView.findViewById(R.id.event_item_date);
            holder.state = (TextView)convertView.findViewById(R.id.event_item_state);
            holder.image = (NetworkImageView)convertView.findViewById(R.id.event_item_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final Event item = getItem(position);
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());
        holder.state.setText("test");

        if(item.getImage_url() != null){
            holder.image.setImageUrl(item.getImage_url(), ImageLoaderSingleton.getImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()) );
            //holder.image.setTag(mImageLoader.get(item.getImageResource(), listener));
        }

        return convertView;
    }

    private class ViewHolder {
        TextView title;
        TextView date;
        TextView state;
        NetworkImageView image;
    }
}
