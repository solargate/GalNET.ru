package ru.solargateteam.galnetru;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ru.solargateteam.galnetru.pref.PrefActivity;
import ru.solargateteam.galnetru.pref.PrefEngine;
import ru.solargateteam.galnetru.services.RadioService;
import ru.solargateteam.galnetru.util.ToolbarColorizeHelper;
import ru.solargateteam.galnetru.util.Util;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int SHOW_PREFERENCES = 1;

    PrefEngine pe;

    MainFragment fragmentMain;

    Button btnPlayRadioSoft;
    Button btnPlayRadioHard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Global.TAG, "Create");
        super.onCreate(savedInstanceState);

        pe = new PrefEngine(this);
        pe.initDefaults(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View drawerHeader = navigationView.inflateHeaderView(R.layout.nav_header_main);

        btnPlayRadioSoft = (Button) drawerHeader.findViewById(R.id.btnPlayRadioSoft);
        btnPlayRadioSoft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetwork(v.getContext())) {
                    Intent intent = new Intent(v.getContext(), RadioService.class);
                    intent.setAction(RadioService.ACTION_SOFT_PLAY);
                    startService(intent);
                } else {
                    Toast toast = Toast.makeText(v.getContext(), R.string.err_no_network, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        btnPlayRadioHard = (Button) drawerHeader.findViewById(R.id.btnPlayRadioHard);
        btnPlayRadioHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetwork(v.getContext())) {
                    Intent intent = new Intent(v.getContext(), RadioService.class);
                    intent.setAction(RadioService.ACTION_HARD_PLAY);
                    startService(intent);
                } else {
                    Toast toast = Toast.makeText(v.getContext(), R.string.err_no_network, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        fragmentMain = new MainFragment();
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fMain, fragmentMain);
        ft.commit();

        final View.OnClickListener originalToolbarListener = toggle.getToolbarNavigationClickListener();

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    toggle.setDrawerIndicatorEnabled(false);
                    toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getSupportFragmentManager().popBackStack();
                        }
                    });
                    ToolbarColorizeHelper.colorizeToolbar((Toolbar) findViewById(R.id.toolbar), getResources().getColor(R.color.colorEDOrange), MainActivity.this);
                } else {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    toggle.setDrawerIndicatorEnabled(true);
                    toggle.setToolbarNavigationClickListener(originalToolbarListener);
                    ToolbarColorizeHelper.colorizeToolbar((Toolbar) findViewById(R.id.toolbar), getResources().getColor(R.color.colorEDOrange), MainActivity.this);
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        ToolbarColorizeHelper.colorizeToolbar((Toolbar) findViewById(R.id.toolbar), getResources().getColor(R.color.colorEDOrange), MainActivity.this);
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

        //ToolbarColorizeHelper.colorizeToolbar((Toolbar) findViewById(R.id.toolbar), getResources().getColor(R.color.colorEDOrange), MainActivity.this);

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
            fragmentMain.setCurrentFeedType(Global.FEED_TYPE_ALL);
            fragmentMain.setNewsRecyclerAdapter(fragmentMain.getCurrentFeedType());
        } else if (id == R.id.nav_feed_galnet_news) {
            fragmentMain.setCurrentFeedType(Global.FEED_TYPE_GALNET_NEWS);
            fragmentMain.setNewsRecyclerAdapter(fragmentMain.getCurrentFeedType());
        } else if (id == R.id.nav_feed_powerplay) {
            fragmentMain.setCurrentFeedType(Global.FEED_TYPE_POWERPLAY);
            fragmentMain.setNewsRecyclerAdapter(fragmentMain.getCurrentFeedType());
        } else if (id == R.id.nav_feed_weekly_report) {
            fragmentMain.setCurrentFeedType(Global.FEED_TYPE_WEEKLY_REPORT);
            fragmentMain.setNewsRecyclerAdapter(fragmentMain.getCurrentFeedType());
        } else if (id == R.id.nav_feed_comm_goals) {
            fragmentMain.setCurrentFeedType(Global.FEED_TYPE_COMM_GOALS);
            fragmentMain.setNewsRecyclerAdapter(fragmentMain.getCurrentFeedType());
        } else if (id == R.id.nav_feed_comm_news) {
            fragmentMain.setCurrentFeedType(Global.FEED_TYPE_COMM_NEWS);
            fragmentMain.setNewsRecyclerAdapter(fragmentMain.getCurrentFeedType());
        } else if (id == R.id.nav_feed_site_news) {
            fragmentMain.setCurrentFeedType(Global.FEED_TYPE_SITE_NEWS);
            fragmentMain.setNewsRecyclerAdapter(fragmentMain.getCurrentFeedType());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Global.NEWS_SERVICE_STATUS_OK) {
            fragmentMain.setSwipeRefreshState(false);
            fragmentMain.setNewsRecyclerAdapter(fragmentMain.getCurrentFeedType());
        } else if (resultCode == Global.NEWS_SERVICE_STATUS_NON) {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.err_no_network, Toast.LENGTH_SHORT);
            toast.show();
            fragmentMain.setSwipeRefreshState(false);
        }
    }

    public void switchPost(int id, Fragment fragment) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(id, fragment);
        ft.addToBackStack(null);
        ft.commit();
   }
}