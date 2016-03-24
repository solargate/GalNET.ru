package ru.solargateteam.galnetru.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import ru.solargateteam.galnetru.db.DBEngine;
import ru.solargateteam.galnetru.Global;
import ru.solargateteam.galnetru.rss.RSSItem;
import ru.solargateteam.galnetru.rss.RSSReader;

public class RSSService extends IntentService {

    public static final String ACTION_FROM_ACTIVITY        = "ru.solargateteam.galnetru.action.FROM_ACTIVITY";
    public final static String PARAM_PINTENT_FROM_ACTIVITY = "ru.solargateteam.galnetru.pendingintent.FROM_ACTIVITY";

    DBEngine dbe;

    public RSSService() {
        super("RSSService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Global.TAG, "RSSService onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Global.TAG, "RSSService onDestroy");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            final String action = intent.getAction();

            if (ACTION_FROM_ACTIVITY.equals(action)) {
                PendingIntent pi = intent.getParcelableExtra(PARAM_PINTENT_FROM_ACTIVITY);
                refreshNews();

                startService(new Intent(RSSService.this, ImageService.class));

                pi.send(Global.NEWS_SERVICE_STATUS_OK);
            }

        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    private void refreshNews() {

        try {

            RSSReader rssReader;
            List<RSSItem> tempItems;

            dbe = new DBEngine(getApplicationContext());

            rssReader = new RSSReader(Global.RSS_FEED_GALNET_NEWS);
            tempItems = rssReader.getItems();
            for (int i = 0; i < tempItems.size(); i++) {
                dbe.insertContentItem(tempItems.get(i), Global.FEED_TYPE_GALNET_NEWS);
            }

            tempItems.clear();

            rssReader = new RSSReader(Global.RSS_FEED_POWERPLAY);
            tempItems = rssReader.getItems();
            for (int i = 0; i < tempItems.size(); i++) {
                dbe.insertContentItem(tempItems.get(i), Global.FEED_TYPE_POWERPLAY);
            }

            tempItems.clear();

            rssReader = new RSSReader(Global.RSS_FEED_WEEKLY_REPORT);
            tempItems = rssReader.getItems();
            for (int i = 0; i < tempItems.size(); i++) {
                dbe.insertContentItem(tempItems.get(i), Global.FEED_TYPE_WEEKLY_REPORT);
            }

            tempItems.clear();

            rssReader = new RSSReader(Global.RSS_FEED_COMM_GOALS);
            tempItems = rssReader.getItems();
            for (int i = 0; i < tempItems.size(); i++) {
                dbe.insertContentItem(tempItems.get(i), Global.FEED_TYPE_COMM_GOALS);
            }

            tempItems.clear();

            rssReader = new RSSReader(Global.RSS_FEED_SITE_NEWS);
            tempItems = rssReader.getItems();
            for (int i = 0; i < tempItems.size(); i++) {
                dbe.insertContentItem(tempItems.get(i), Global.FEED_TYPE_SITE_NEWS);
            }

            dbe.close();

        } catch (Exception e) {
            Log.e(Global.TAG, e.getMessage());
        }
    }
}
