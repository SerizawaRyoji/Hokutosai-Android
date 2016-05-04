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
 * Created by ryoji on 2016/05/02.
 */
public class ShopItemAdapter extends BaseAdapter{

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Shop> shopList;
    private ViewHolder holder = null;

    public ShopItemAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setShopList(ArrayList<Shop> itemList) {
        this.shopList = itemList;
    }

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

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.name.setText(shopList.get(position).getName());
        holder.tenant.setText(shopList.get(position).getTenant());
        holder.sales.setText(shopList.get(position).getSales());

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
    }
}
