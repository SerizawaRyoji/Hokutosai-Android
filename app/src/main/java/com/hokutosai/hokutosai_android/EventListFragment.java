package com.hokutosai.hokutosai_android;

import android.app.Activity;
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

    static final int REQUEST_EVENT_CODE = 2124;

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
        startActivityForResult( i, REQUEST_EVENT_CODE );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {    //詳細画面から戻ってきたときに呼ばれる
        super.onActivityResult(requestCode, resultCode, data);

        // startActivityForResult()の際に指定した識別コードとの比較
        if( requestCode == REQUEST_EVENT_CODE ){
            // 返却結果ステータスとの比較
            if( resultCode == Activity.RESULT_OK ){

                // 返却されてきたintentから値を取り出す
                EventDetailActivity.EventDetail event = (EventDetailActivity.EventDetail)data.getSerializableExtra("EventDetail");

                if(getActivity() != null){
                    for(int i=0 ; i<list.size() ; ++i){
                        if(event.event_id == list.get(i).getEvent_id()){

                            if( event.liked && !list.get(i).getLiked() || !event.liked && list.get(i).getLiked()){
                                list.get(i).setLiked( event.liked );    //いいねの状態が変わっていたら更新
                                list.get(i).setLikes_count( event.likes_count );
                                //setListAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                            break;
                        }
                    }
                }

            }
        }
    }

    @Override
    public void onStop() {      //ここでwepAPIの読み込みを注視する文を書きたい
        // TODO 自動生成されたメソッド・スタブ
        super.onStop();
    }
}
