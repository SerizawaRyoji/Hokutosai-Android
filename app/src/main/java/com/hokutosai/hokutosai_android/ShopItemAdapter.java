package com.hokutosai.hokutosai_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ryoji on 2016/05/02.
 */
public class ShopItemAdapter extends BaseAdapter{

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<ShopItem> shopItemList;

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

        /*final ViewHolder holder;
        if (convertView == null) {	//取得すべきビューがnullなら
            convertView = mInflater.inflate(R.layout.shop_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView)convertView.findViewById(R.id.list_shop_title);
            holder.tenant = (TextView)convertView.findViewById(R.id.list_shop_tenant);
            holder.sales = (TextView)convertView.findViewById(R.id.list_shop_sales);
            holder.image = (NetworkImageView)convertView.findViewById(R.id.list_shop_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        final ShopItem item = getItem(position);
        holder.name.setText(item.getName());
        holder.tenant.setText(item.getTenant());
        holder.sales.setText(item.getSales());

        if(item.getImage_url() != null){
            holder.image.setImageUrl(item.getImage_url(), new ImageLoader(RequestQueueSingleton.getInstance(), LruCacheSingleton.getInstance()));
            //holder.image.setTag(mImageLoader.get(item.getImageResource(), listener));
        }

        return convertView;*/


        convertView = layoutInflater.inflate(R.layout.shop_item,parent,false);

        ((TextView)convertView.findViewById(R.id.shop_item_name)).setText(shopItemList.get(position).getName());
        ((TextView)convertView.findViewById(R.id.shop_item_tenant)).setText(shopItemList.get(position).getTenant());
        ((TextView)convertView.findViewById(R.id.shop_item_sales)).setText(shopItemList.get(position).getSales());

        return convertView;
    }

    /*private class ViewHolder {
        TextView name;
        TextView tenant;
        TextView sales;
        NetworkImageView image;
    }*/
}
