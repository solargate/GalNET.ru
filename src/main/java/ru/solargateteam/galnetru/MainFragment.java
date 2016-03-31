package ru.solargateteam.galnetru;

import android.support.v4.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ru.solargateteam.galnetru.db.DBEngine;
import ru.solargateteam.galnetru.services.RSSService;

public class MainFragment extends Fragment{

    DBEngine dbe;

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    NewsRecyclerAdapter mAdapter;

    SwipeRefreshLayout mSwipeRefreshLayout;

    protected String currentFeedType;

    public String getCurrentFeedType() {
        return currentFeedType;
    }

    public void setCurrentFeedType(String currentFeedType) {
        this.currentFeedType = currentFeedType;
    }

    public void setNewsRecyclerAdapter(String feedType) {
        mAdapter = new NewsRecyclerAdapter(dbe.readContent(feedType));
        mRecyclerView.setAdapter(mAdapter);
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

        setCurrentFeedType(Global.FEED_TYPE_ALL);

        Log.d(Global.TAG, "1");

        dbe = new DBEngine(getActivity());

        Log.d(Global.TAG, "2");

        mRecyclerView = (RecyclerView) v.findViewById(R.id.news_recycler_view);

        Log.d(Global.TAG, "3");

        mLayoutManager = new LinearLayoutManager(v.getContext());

        Log.d(Global.TAG, "4");

        mRecyclerView.setLayoutManager(mLayoutManager);

        Log.d(Global.TAG, "5");

        setNewsRecyclerAdapter(getCurrentFeedType());

        Log.d(Global.TAG, "6");

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.content_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });

        Log.d(Global.TAG, "7");

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Global.NEWS_SERVICE_STATUS_OK) {
            mSwipeRefreshLayout.setRefreshing(false);
            setNewsRecyclerAdapter(getCurrentFeedType());
        } else if (resultCode == Global.NEWS_SERVICE_STATUS_NON) {
            Toast toast = Toast.makeText(getActivity(), R.string.err_no_network, Toast.LENGTH_SHORT);
            toast.show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        dbe.updateNewPostToOld();
    }
}
