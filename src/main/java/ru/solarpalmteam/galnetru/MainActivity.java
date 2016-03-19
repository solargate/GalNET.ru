package ru.solarpalmteam.galnetru;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ru.solarpalmteam.galnetru.rss.RSSMaster;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseEngine de;

    RecyclerView mRecyclerView;
    NewsRecyclerAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    protected String currentFeedType;

    public String getCurrentFeedType() {
        return currentFeedType;
    }

    public void setCurrentFeedType(String currentFeedType) {
        this.currentFeedType = currentFeedType;
    }

    private void setNewsRecyclerAdapter(String feedType) {
        mAdapter = new NewsRecyclerAdapter(de.readContent(feedType));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Global.TAG, "Create");
        super.onCreate(savedInstanceState);

        de = new DatabaseEngine(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        setCurrentFeedType(Global.FEED_TYPE_ALL);
        setNewsRecyclerAdapter(getCurrentFeedType());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: TEST. Обработчик - чтение RSS
                RSSMaster rssMaster = new RSSMaster(getApplicationContext());
                rssMaster.startReadingRSS();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNewsRecyclerAdapter(getCurrentFeedType());
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
}
