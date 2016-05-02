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

            TextView shopName = (TextView) convertView.findViewById(R.id.shop_item_name);
            TextView shopTenant = (TextView) convertView.findViewById(R.id.shop_item_tenant);
            TextView shopSales = (TextView) convertView.findViewById(R.id.shop_item_sales);

            shopName.setText(shopItemList.get(position).getName());
            shopTenant.setText(shopItemList.get(position).getTenant());
            shopSales.setText(shopItemList.get(position).getSales());

            holder = new ViewHolder();
            holder.name = shopName;
            holder.tenant = shopTenant;
            holder.sales = shopSales;

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        // リクエストのキャンセル処理
        /*ImageLoader.ImageContainer imageContainer = (ImageLoader.ImageContainer)holder.imageView.getTag();
        if (imageContainer != null) {
            imageContainer.cancelRequest();
        }

        if (shopItemList.get(position).getImage_url() != null) {
            NetworkImageView imageView = (NetworkImageView) convertView.findViewById(R.id.shop_item_image);
            imageView.setImageUrl(shopItemList.get(position).getImage_url(), mImageLoader);
            holder.image.setImageUrl(item.getImageResource(), new ImageLoader(RequestQueueSingleton.getInstance(), LruBitmapCache.getInstance()));
            //holder.image.setTag(mImageLoader.get(item.getImageResource(), listener));
        }*/

        return convertView;
    }

    private class ViewHolder{
        TextView name;
        TextView tenant;
        TextView sales;
    }
}
