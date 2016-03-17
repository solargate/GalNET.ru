package ru.solarpalmteam.galnetru.rss;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import ru.solarpalmteam.galnetru.DatabaseEngine;
import ru.solarpalmteam.galnetru.Global;

public class RSSProcessTask extends AsyncTask<String, Void, List<RSSItem> > {

    DatabaseEngine de;

    private Context mContext;

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected List<RSSItem> doInBackground(String... urls) {

        try {
            RSSReader rssReader = new RSSReader(urls[0]);
            return  rssReader.getItems();
        } catch (Exception e) {
            Log.e(Global.TAG, e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<RSSItem> rssItems) {

        for (int i = 0; i < rssItems.size(); i++)
            Log.i(Global.TAG, rssItems.get(i).getTitle());

        de = new DatabaseEngine(mContext);

        for (int i = 0; i < rssItems.size(); i++) {
            de.insertContentItem(rssItems.get(i));
        }
    }
}
