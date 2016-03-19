package ru.solargateteam.galnetru.rss;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import ru.solargateteam.galnetru.DatabaseEngine;
import ru.solargateteam.galnetru.Global;

public class RSSProcessTask extends AsyncTask<Void, Void, String /*List<RSSItem>*/ > {

    DatabaseEngine de;

    private Context mContext;

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(Void... params) {

        try {

            RSSReader rssReader;
            List<RSSItem> tempItems;

            de = new DatabaseEngine(mContext);

            rssReader = new RSSReader(Global.RSS_FEED_GALNET_NEWS);
            tempItems = rssReader.getItems();
            for (int i = 0; i < tempItems.size(); i++) {
                de.insertContentItem(tempItems.get(i), Global.FEED_TYPE_GALNET_NEWS);
            }

            tempItems.clear();

            rssReader = new RSSReader(Global.RSS_FEED_POWERPLAY);
            tempItems = rssReader.getItems();
            for (int i = 0; i < tempItems.size(); i++) {
                de.insertContentItem(tempItems.get(i), Global.FEED_TYPE_POWERPLAY);
            }

            tempItems.clear();

            rssReader = new RSSReader(Global.RSS_FEED_WEEKLY_REPORT);
            tempItems = rssReader.getItems();
            for (int i = 0; i < tempItems.size(); i++) {
                de.insertContentItem(tempItems.get(i), Global.FEED_TYPE_WEEKLY_REPORT);
            }

            tempItems.clear();

            rssReader = new RSSReader(Global.RSS_FEED_COMM_GOALS);
            tempItems = rssReader.getItems();
            for (int i = 0; i < tempItems.size(); i++) {
                de.insertContentItem(tempItems.get(i), Global.FEED_TYPE_COMM_GOALS);
            }

            tempItems.clear();

            rssReader = new RSSReader(Global.RSS_FEED_SITE_NEWS);
            tempItems = rssReader.getItems();
            for (int i = 0; i < tempItems.size(); i++) {
                de.insertContentItem(tempItems.get(i), Global.FEED_TYPE_SITE_NEWS);
            }

            return "OK";

        } catch (Exception e){
                Log.e(Global.TAG, e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
