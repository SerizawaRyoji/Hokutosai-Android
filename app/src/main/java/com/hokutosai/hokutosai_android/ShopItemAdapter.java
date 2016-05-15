package com.hokutosai.hokutosai_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by ryoji on 2016/05/02.
 */
public class ShopItemAdapter extends BaseAdapter{

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Shop> shopList;
    private ViewHolder holder = null;
    private int sales_width = 0;        //salesの文字の幅　＊いいねと重ならないようにする必要があるため計算された幅を保持

    public ShopItemAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setShopList(ArrayList<Shop> itemList) {
        this.shopList = itemList;
    }

    public void setSalesWidth(int pixel){ sales_width = pixel; }

    @Override
    public int getCount() {
        return shopList.size();
    }

    @Override
    public Object getItem(int position) {
        return shopList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return shopList.get(position).getShop_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.shop_item, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.shop_item_name);
            holder.tenant = (TextView) convertView.findViewById(R.id.shop_item_tenant);
            holder.sales = (TextView) convertView.findViewById(R.id.shop_item_sales);
            holder.image = (NetworkImageView) convertView.findViewById(R.id.shop_item_image);
            holder.like_icon = (ImageView) convertView.findViewById(R.id.shop_item_like);
            holder.likes_count = (TextView) convertView.findViewById(R.id.shop_item_like_count);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.sales.setWidth(sales_width);

        holder.name.setText(shopList.get(position).getName());
        holder.tenant.setText(shopList.get(position).getTenant());
        holder.sales.setText(shopList.get(position).getSales());
        holder.like_icon.setSelected( shopList.get(position).getLiked() );
        holder.likes_count.setText( String.valueOf(shopList.get(position).getLikes_count()) );

        if (shopList.get(position).getImage_url() != null) {
            holder.image.setImageUrl(shopList.get(position).getImage_url(), ImageLoaderSingleton.getImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()) );
        }

        return convertView;
    }

    private class ViewHolder{
        TextView name;
        TextView tenant;
        TextView sales;
        NetworkImageView image;
        TextView likes_count;
        ImageView like_icon;
    }
}
