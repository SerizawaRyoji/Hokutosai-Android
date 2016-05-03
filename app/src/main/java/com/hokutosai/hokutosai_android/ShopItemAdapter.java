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
    ArrayList<ShopItem> shopItemList;
    private ViewHolder holder = null;

    public ShopItemAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setShopItemList(ArrayList<ShopItem> itemList) {
        this.shopItemList = itemList;
    }

    @Override
    public int getCount() {
        return shopItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return shopItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return shopItemList.get(position).getShop_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.shop_item, null);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.shop_item_name);
            holder.tenant = (TextView) convertView.findViewById(R.id.shop_item_tenant);
            holder.sales = (TextView) convertView.findViewById(R.id.shop_item_sales);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.name.setText(shopItemList.get(position).getName());
        holder.tenant.setText(shopItemList.get(position).getTenant());
        holder.sales.setText(shopItemList.get(position).getSales());

        if (shopItemList.get(position).getImage_url() != null) {
            NetworkImageView imageView = (NetworkImageView) convertView.findViewById(R.id.shop_item_image);
            imageView.setImageUrl(shopItemList.get(position).getImage_url(), ImageLoaderSingleton.getImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()) );
        }

        return convertView;
    }

    private class ViewHolder{
        TextView name;
        TextView tenant;
        TextView sales;
    }
}
