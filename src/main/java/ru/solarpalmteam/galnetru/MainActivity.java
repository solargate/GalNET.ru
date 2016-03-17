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

import java.util.List;

import ru.solarpalmteam.galnetru.rss.RSSItem;
import ru.solarpalmteam.galnetru.rss.RSSProcessTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView mRecyclerView;
    NewsRecyclerAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    DatabaseEngine de;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Global.TAG, "Create");
        super.onCreate(savedInstanceState);

        de = new DatabaseEngine(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        List<RSSItem> listContent = de.readContentAll();

        Log.d(Global.TAG, "!!! LIST: " + listContent);

        for (int i = 0; i < listContent.size(); i++)
            Log.d(Global.TAG, "!!! READ: " + listContent.get(i).getTitle());
        */

        mRecyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NewsRecyclerAdapter(de.readContentAll()); // !!!
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: TEST. Обработчик - чтение RSS
                RSSProcessTask task = new RSSProcessTask();
                task.setContext(getApplicationContext());
                task.execute(Global.RSS_FEED_GALNET_NEWS);

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

        /*
        Log.d(Global.TAG, "Resume");
        de = new DatabaseEngine(this);

        List<RSSItem> listContent = de.readContentAll();

        Log.d(Global.TAG, "!!! LIST: " + listContent);

        for (int i = 0; i < listContent.size(); i++)
            Log.d(Global.TAG, "!!! READ: " + listContent.get(i).getTitle());
        */

        //mRecyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
        //mAdapter = new NewsRecyclerAdapter(de.readContentAll()); // !!!
        //mRecyclerView.setAdapter(mAdapter);

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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
