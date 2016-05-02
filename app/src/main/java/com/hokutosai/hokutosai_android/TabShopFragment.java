package com.hokutosai.hokutosai_android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by ryoji on 2016/05/01.
 */
public class TabShopFragment  extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO 自動生成されたメソッド・スタブ
        return inflater.inflate(R.layout.tab_shop_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        ListView listView = (ListView)getActivity().findViewById(R.id.list_shop_view);

        ArrayList<ShopItem> list = new ArrayList<>();
        ShopItem s = new ShopItem(); s.setName("test"); s.setTenant("tenant");
        list.add(s);

        ShopItemAdapter adapter = new ShopItemAdapter( getActivity() );
        adapter.setShopItemList(list);
        listView.setAdapter(adapter);
    }
}
