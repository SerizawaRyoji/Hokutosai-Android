package com.hokutosai.hokutosai_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by ryoji on 2016/05/04.
 */
public class EventListFragment extends ListFragment {

    private List<Event> list;
    private ArrayAdapter<Event> adapter;

    public void setEventListFragment( List<Event> list){
        this.list = list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        adapter=null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if(list.isEmpty()) Log.d("test","list is empty");

        if(adapter == null && !list.isEmpty()) {
            adapter = new EventItemAdapter(getActivity(), list, R.layout.event_item);
            setListAdapter(adapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // TODO 自動生成されたメソッド・スタブ
        super.onListItemClick(l, v, position, id);

        Intent i = new Intent(getActivity(), EventDetailActivity.class);
        i.putExtra("Event", list.get(position));
        startActivity(i);
    }

    @Override
    public void onStop() {      //ここでwepAPIの読み込みを注視する文を書きたい
        // TODO 自動生成されたメソッド・スタブ
        super.onStop();
    }
}
