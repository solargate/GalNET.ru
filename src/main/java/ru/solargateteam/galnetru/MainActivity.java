package ru.solargateteam.galnetru;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ru.solargateteam.galnetru.db.DBEngine;
import ru.solargateteam.galnetru.pref.PrefActivity;
import ru.solargateteam.galnetru.pref.PrefEngine;
import ru.solargateteam.galnetru.services.RSSService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int SHOW_PREFERENCES = 1;

    DBEngine dbe;
    PrefEngine pe;

    RecyclerView mRecyclerView;
    NewsRecyclerAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    SwipeRefreshLayout mSwipeRefreshLayout;

    protected String currentFeedType;

    public String getCurrentFeedType() {
        return currentFeedType;
    }

    public void setCurrentFeedType(String currentFeedType) {
        this.currentFeedType = currentFeedType;
    }

    private void setNewsRecyclerAdapter(String feedType) {
        mAdapter = new NewsRecyclerAdapter(dbe.readContent(feedType));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void refreshNews() {
        PendingIntent pi;
        Intent intent;

        pi = createPendingResult(Global.NEWS_SERVICE_TASK_CODE, new Intent(), 0);
        intent = new Intent(this, RSSService.class);
        intent.setAction(RSSService.ACTION_FROM_ACTIVITY);
        intent.putExtra(RSSService.PARAM_PINTENT_FROM_ACTIVITY, pi);

        startService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Global.TAG, "Create");
        super.onCreate(savedInstanceState);

        dbe = new DBEngine(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        setCurrentFeedType(Global.FEED_TYPE_ALL);
        setNewsRecyclerAdapter(getCurrentFeedType());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.content_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });

        pe = new PrefEngine(this);
        pe.initDefaults(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNewsRecyclerAdapter(getCurrentFeedType());
    }

    @Override
    protected void onStop() {
        super.onStop();
        dbe.updateNewPostToOld();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, PrefActivity.class);
            startActivityForResult(intent, SHOW_PREFERENCES);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_feed_all) {
            setCurrentFeedType(Global.FEED_TYPE_ALL);
            setNewsRecyclerAdapter(getCurrentFeedType());
        } else if (id == R.id.nav_feed_galnet_news) {
            setCurrentFeedType(Global.FEED_TYPE_GALNET_NEWS);
            setNewsRecyclerAdapter(getCurrentFeedType());
        } else if (id == R.id.nav_feed_powerplay) {
            setCurrentFeedType(Global.FEED_TYPE_POWERPLAY);
            setNewsRecyclerAdapter(getCurrentFeedType());
        } else if (id == R.id.nav_feed_weekly_report) {
            setCurrentFeedType(Global.FEED_TYPE_WEEKLY_REPORT);
            setNewsRecyclerAdapter(getCurrentFeedType());
        } else if (id == R.id.nav_feed_comm_goals) {
            setCurrentFeedType(Global.FEED_TYPE_COMM_GOALS);
            setNewsRecyclerAdapter(getCurrentFeedType());
        } else if (id == R.id.nav_feed_site_news) {
            setCurrentFeedType(Global.FEED_TYPE_SITE_NEWS);
            setNewsRecyclerAdapter(getCurrentFeedType());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Global.NEWS_SERVICE_STATUS_OK) {
            mSwipeRefreshLayout.setRefreshing(false);
            setNewsRecyclerAdapter(getCurrentFeedType());
        } else if (resultCode == Global.NEWS_SERVICE_STATUS_NON) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.err_no_network, Toast.LENGTH_SHORT);
            toast.show();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}