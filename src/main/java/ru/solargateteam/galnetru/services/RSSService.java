package ru.solargateteam.galnetru.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import ru.solargateteam.galnetru.util.AlarmSetter;
import ru.solargateteam.galnetru.util.NotificationEngine;
import ru.solargateteam.galnetru.util.Util;
import ru.solargateteam.galnetru.db.DBEngine;
import ru.solargateteam.galnetru.Global;
import ru.solargateteam.galnetru.rss.RSSItem;
import ru.solargateteam.galnetru.rss.RSSReader;

public class RSSService extends IntentService {

    public static final String ACTION_FROM_ACTIVITY        = "ru.solargateteam.galnetru.action.FROM_ACTIVITY";
    public final static String PARAM_PINTENT_FROM_ACTIVITY = "ru.solargateteam.galnetru.pendingintent.FROM_ACTIVITY";
    public static final String ACTION_FROM_ALARM           = "ru.solargateteam.galnetru.action.FROM_ALARM";
    public static final String ACTION_FROM_BOOT            = "ru.solargateteam.galnetru.action.FROM_BOOT";

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

        Context context = getApplicationContext();

        try {
            final String action = intent.getAction();

            if (ACTION_FROM_ACTIVITY.equals(action)) {
                PendingIntent pi = intent.getParcelableExtra(PARAM_PINTENT_FROM_ACTIVITY);

                if (Util.isNetwork(context)) {
                    refreshNews();
                    startService(new Intent(RSSService.this, ImageService.class));
                    pi.send(Global.NEWS_SERVICE_STATUS_OK);
                } else {
                    pi.send(Global.NEWS_SERVICE_STATUS_NON);
                }
            } else if (ACTION_FROM_ALARM.equals(action) || ACTION_FROM_BOOT.equals(action)) {

                if (Util.isNetwork(context)) {

                    refreshNews();

                    NotificationEngine ne = new NotificationEngine(context);
                    ne.processNotificationNewPost(context);

                    startService(new Intent(RSSService.this, ImageService.class));
                }
            }
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }

        AlarmSetter as = new AlarmSetter();
        as.setRefreshAlarm(context);
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

            //tempItems.clear();

            //rssReader = new RSSReader(Global.RSS_FEED_WEEKLY_REPORT);
            //tempItems = rssReader.getItems();
            //for (int i = 0; i < tempItems.size(); i++) {
            //    dbe.insertContentItem(tempItems.get(i), Global.FEED_TYPE_WEEKLY_REPORT);
            //}

            tempItems.clear();

            rssReader = new RSSReader(Global.RSS_FEED_COMM_GOALS);
            tempItems = rssReader.getItems();
            for (int i = 0; i < tempItems.size(); i++) {
                dbe.insertContentItem(tempItems.get(i), Global.FEED_TYPE_COMM_GOALS);
            }

            tempItems.clear();

            rssReader = new RSSReader(Global.RSS_FEED_COMM_NEWS);
            tempItems = rssReader.getItems();
            for (int i = 0; i < tempItems.size(); i++) {
                dbe.insertContentItem(tempItems.get(i), Global.FEED_TYPE_COMM_NEWS);
            }

            tempItems.clear();

            rssReader = new RSSReader(Global.RSS_FEED_EMPIRE);
            tempItems = rssReader.getItems();
            for (int i = 0; i < tempItems.size(); i++) {
                dbe.insertContentItem(tempItems.get(i), Global.FEED_TYPE_EMPIRE);
            }

            tempItems.clear();

            rssReader = new RSSReader(Global.RSS_FEED_FEDERATION);
            tempItems = rssReader.getItems();
            for (int i = 0; i < tempItems.size(); i++) {
                dbe.insertContentItem(tempItems.get(i), Global.FEED_TYPE_FEDERATION);
            }

            tempItems.clear();

            rssReader = new RSSReader(Global.RSS_FEED_ALLIANCE);
            tempItems = rssReader.getItems();
            for (int i = 0; i < tempItems.size(); i++) {
                dbe.insertContentItem(tempItems.get(i), Global.FEED_TYPE_ALLIANCE);
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
