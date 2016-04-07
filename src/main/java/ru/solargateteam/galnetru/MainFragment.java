package ru.solargateteam.galnetru;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.solargateteam.galnetru.db.DBEngine;
import ru.solargateteam.galnetru.services.RSSService;

public class MainFragment extends Fragment {

    DBEngine dbe;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    NewsRecyclerAdapter mAdapter;

    SwipeRefreshLayout mSwipeRefreshLayout;

    private String currentFeedType;

    public String getCurrentFeedType() {

        if (currentFeedType == null)
            this.currentFeedType = Global.FEED_TYPE_ALL;

        return currentFeedType;
    }

    public void setCurrentFeedType(String currentFeedType) {
        this.currentFeedType = currentFeedType;
    }

    public void setNewsRecyclerAdapter() {
        mAdapter = new NewsRecyclerAdapter(getContext(), dbe.readContent(getCurrentFeedType()));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setSwipeRefreshState(boolean state) {
        mSwipeRefreshLayout.setRefreshing(state);
    }

    private void refreshNews() {
        PendingIntent pi;
        Intent intent;

        pi = getActivity().createPendingResult(Global.NEWS_SERVICE_TASK_CODE, new Intent(), 0);
        intent = new Intent(getActivity(), RSSService.class);
        intent.setAction(RSSService.ACTION_FROM_ACTIVITY);
        intent.putExtra(RSSService.PARAM_PINTENT_FROM_ACTIVITY, pi);

        getActivity().startService(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, null);
        dbe = new DBEngine(getActivity());
        mRecyclerView = (RecyclerView) v.findViewById(R.id.news_recycler_view);
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        setNewsRecyclerAdapter();

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.content_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
        dbe.updateNewPostToOld();
    }
}
