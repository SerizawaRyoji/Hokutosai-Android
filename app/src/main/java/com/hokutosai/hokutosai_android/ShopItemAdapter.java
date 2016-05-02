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
        convertView = layoutInflater.inflate(R.layout.shop_item,parent,false);

        ((TextView)convertView.findViewById(R.id.name)).setText(shopItemList.get(position).getName());
        ((TextView)convertView.findViewById(R.id.tenant)).setText(shopItemList.get(position).getTenant());

        return convertView;
    }
}
