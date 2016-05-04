package com.hokutosai.hokutosai_android;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.uphyca.android.loopviewpager.LoopViewPager;

/**
 * Created by ryoji on 2016/05/04.
 */
public class EventTopicFragment extends Fragment {

    private final static int[] COLOR_LIST = {0xff7cd5aa, 0xfff1e6a2, 0xfffecc5a, 0xffff8b58, 0xffe92440};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_event_topic, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final LoopViewPager viewPager = (LoopViewPager) getActivity().findViewById(R.id.event_topic_view_pager);
        //final LxIndicatorGroup indicatorGroup = (LxIndicatorGroup) findViewById(R.id.indicator_view);
        //indicatorGroup.setup(VIEW_COUNT, R.drawable.indicator);
        MyViewPagerAdapter adapter = new MyViewPagerAdapter();
        for (int color : COLOR_LIST) {
            FrameLayout view = new FrameLayout(getActivity());
            view.setBackgroundColor(color);
            adapter.addView(view);
        }
        viewPager.setAdapter(adapter);
        //viewPager.addOnPageChangeListener(indicatorGroup.getPositionSyncListener());
    }
}
