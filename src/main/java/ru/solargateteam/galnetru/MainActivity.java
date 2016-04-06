package ru.solargateteam.galnetru;

import android.animation.ObjectAnimator;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
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
import ru.solargateteam.galnetru.util.ToolbarColorizer;
import ru.solargateteam.galnetru.util.Util;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int SHOW_PREFERENCES = 1;

    PrefEngine pe;

    MainFragment fragmentMain;

    ProgressDialog progressDialog;

    Button btnPlayRadioSoft;
    Button btnPlayRadioHard;

    private String currentRadioType;

    public String getCurrentRadioType() {

        Log.d(Global.TAG, "getCurrentRadioType - " + currentRadioType);

        return currentRadioType;
    }

    public void setCurrentRadioType(String currentRadioType) {
        this.currentRadioType = currentRadioType;

        Log.d(Global.TAG, "setCurrentRadioType - " + currentRadioType);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Global.TAG, "Create");
        super.onCreate(savedInstanceState);

        pe = new PrefEngine(this);
        pe.initDefaults(this);

        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                playRadio(RadioService.ACTION_SOFT_PLAY);
            }
        });

        btnPlayRadioHard = (Button) drawerHeader.findViewById(R.id.btnPlayRadioHard);
        btnPlayRadioHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRadio(RadioService.ACTION_HARD_PLAY);
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

                } else {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    toggle.setDrawerIndicatorEnabled(true);
                    toggle.setToolbarNavigationClickListener(originalToolbarListener);
                }
                DrawerArrowDrawable drawerArrow = new DrawerArrowDrawable(getBaseContext());
                drawerArrow.setColor(getResources().getColor(R.color.colorEDOrange));
                toolbar.setNavigationIcon(drawerArrow);
                boolean drawerState = getSupportFragmentManager().getBackStackEntryCount() == 0;
                ObjectAnimator.ofFloat(drawerArrow, "progress", drawerState ? 0 : 1).start();
            }
        });

        ToolbarColorizer.colorizeToolbar((Toolbar) findViewById(R.id.toolbar), getResources().getColor(R.color.colorEDOrange), MainActivity.this);

        if (savedInstanceState != null) {
            setCurrentRadioType(savedInstanceState.getString("currentRadioType"));
        }

        syncRadioButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(Global.TAG, "onResume");

        syncRadioButtons();

/*
        if (RadioService.ACTION_SOFT_PLAY.equals(getCurrentRadioType())) {
            btnPlayRadioSoft.setBackgroundResource(R.drawable.ic_menu_radio_soft_play);
            btnPlayRadioHard.setBackgroundResource(R.drawable.ic_menu_radio_hard);
        } else if (RadioService.ACTION_HARD_PLAY.equals(getCurrentRadioType())) {
            btnPlayRadioSoft.setBackgroundResource(R.drawable.ic_menu_radio_soft);
            btnPlayRadioHard.setBackgroundResource(R.drawable.ic_menu_radio_hard_play);
        }
*/
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        Log.d(Global.TAG, "onPostResume");

        //ToolbarColorizer.colorizeToolbar((Toolbar) findViewById(R.id.toolbar), getResources().getColor(R.color.colorEDOrange), MainActivity.this);
/*
        if (RadioService.ACTION_SOFT_PLAY.equals(getCurrentRadioType())) {
            btnPlayRadioSoft.setBackgroundResource(R.drawable.ic_menu_radio_soft_play);
            btnPlayRadioHard.setBackgroundResource(R.drawable.ic_menu_radio_hard);
        } else if (RadioService.ACTION_HARD_PLAY.equals(getCurrentRadioType())) {
            btnPlayRadioSoft.setBackgroundResource(R.drawable.ic_menu_radio_soft);
            btnPlayRadioHard.setBackgroundResource(R.drawable.ic_menu_radio_hard_play);
        }
*/

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentRadioType", getCurrentRadioType());


        Log.d(Global.TAG, "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        setCurrentRadioType(savedInstanceState.getString("currentRadioType"));

        Log.d(Global.TAG, "onRestoreInstanceState");
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
        } else if (resultCode == Global.RADIO_SERVICE_STATUS_START) {

            syncRadioButtons();

            progressDialog = ProgressDialog.show(MainActivity.this, getString(R.string.radio_buffering_title), getString(R.string.radio_buffering_descriprion), true);
        } else if (resultCode == Global.RADIO_SERVICE_STATUS_PREP) {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

        } else if (resultCode == Global.RADIO_SERVICE_STATUS_STOP) {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();

            syncRadioButtons();

            //btnPlayRadioSoft.setBackgroundResource(R.drawable.ic_menu_radio_soft);
            //btnPlayRadioHard.setBackgroundResource(R.drawable.ic_menu_radio_hard);
            //setCurrentRadioType("");
        }
    }

    public void switchPost(int id, Fragment fragment) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.replace(id, fragment);
        ft.addToBackStack(null);
        ft.commit();
   }

    private void playRadio(String action) {
        if (Util.isNetwork(MainActivity.this)) {

            if (action.equals(getCurrentRadioType()))
                setCurrentRadioType("");
            else
            setCurrentRadioType(action);

            PendingIntent pi;

            Log.d(Global.TAG, "1");

            pi = createPendingResult(Global.RADIO_SERVICE_TASK_CODE, new Intent(), 0);

            Log.d(Global.TAG, "2");

            Intent intent = new Intent(MainActivity.this, RadioService.class);

            Log.d(Global.TAG, "3");

            intent.setAction(action);

            Log.d(Global.TAG, "4");

            intent.putExtra(RadioService.PARAM_PINTENT_FROM_ACTIVITY, pi);

            Log.d(Global.TAG, "5");

            startService(intent);

        } else {

            syncRadioButtons();

            Toast toast = Toast.makeText(MainActivity.this, R.string.err_no_network, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void syncRadioButtons() {

        if (RadioService.ACTION_SOFT_PLAY.equals(getCurrentRadioType())) {

            Log.d(Global.TAG, "ACTION_SOFT_PLAY");

            btnPlayRadioSoft.setBackgroundResource(R.drawable.ic_menu_radio_soft_play);
            btnPlayRadioHard.setBackgroundResource(R.drawable.ic_menu_radio_hard);
        } else if (RadioService.ACTION_HARD_PLAY.equals(getCurrentRadioType())) {

            Log.d(Global.TAG, "ACTION_HARD_PLAY");

            btnPlayRadioSoft.setBackgroundResource(R.drawable.ic_menu_radio_soft);
            btnPlayRadioHard.setBackgroundResource(R.drawable.ic_menu_radio_hard_play);
        } else if ("".equals(getCurrentRadioType())) {
            btnPlayRadioSoft.setBackgroundResource(R.drawable.ic_menu_radio_soft);
            btnPlayRadioHard.setBackgroundResource(R.drawable.ic_menu_radio_hard);

        }

    }
}