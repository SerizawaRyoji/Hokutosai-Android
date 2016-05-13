package com.hokutosai.hokutosai_android;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.Date;
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
            holder.liked = (ImageView)convertView.findViewById(R.id.event_item_like);
            holder.likes_count = (TextView)convertView.findViewById(R.id.event_item_like_count);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final Event item = getItem(position);
        holder.title.setText(item.getTitle());
        holder.date.setText(MyDateFormatSingleton.getInstance().getEventDateTime( item.getDate(), item.getStart_time(), item.getEnd_time() ) );

        String stateStr = getEventState(item);
        if( stateStr.equals("開催中!!") ) holder.state.setTextColor(Color.RED);
        else if( !stateStr.equals("終了しました") && !stateStr.isEmpty() ) holder.state.setTextColor(Color.GREEN);
        else holder.state.setTextColor(Color.GRAY);
        holder.state.setText(stateStr);

        holder.liked.setSelected( item.getLiked() );
        holder.likes_count.setText( String.valueOf(item.getLikes_count()) );

        holder.image.setErrorImageResId(R.mipmap.no_image_500x200);
        if(item.getImage_url() != null){
            holder.image.setImageUrl(item.getImage_url(), ImageLoaderSingleton.getImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()) );
            //holder.image.setTag(mImageLoader.get(item.getImageResource(), listener));
        } else{
            holder.image.setImageResource(R.mipmap.no_image_500x200);
        }

        return convertView;
    }

    public String getEventState(final Event item){

        long minS = MyDateFormatSingleton.getInstance().getDiffMinutes( new Date(), item.getDate(), item.getStart_time());   //イベント開始まであと何分かを取得
        long minE = MyDateFormatSingleton.getInstance().getDiffMinutes( new Date(), item.getDate(), item.end_time);          //イベント終了まであと何分かを取得

        if(minS <= 0 && minE >= 0) return "開催中!!";
        else if(minE <= 0) return "終了しました";
        else if(minS >= 0 && minS <= 60) return "開始まであと" + String.valueOf(minS) + "分";
        else return "";
    }

    private class ViewHolder {
        TextView title;
        TextView date;
        TextView state;
        NetworkImageView image;
        ImageView liked;
        TextView likes_count;
    }
}
