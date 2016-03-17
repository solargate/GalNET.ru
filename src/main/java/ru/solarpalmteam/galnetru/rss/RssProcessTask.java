package ru.solarpalmteam.galnetru.rss;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import ru.solarpalmteam.galnetru.Global;

public class RssProcessTask extends AsyncTask<String, Void, List<RssItem> > {

    @Override
    protected List<RssItem> doInBackground(String... urls) {

        try {
            RssReader rssReader = new RssReader(urls[0]);
            return  rssReader.getItems();
        } catch (Exception e) {
            Log.e(Global.TAG, e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<RssItem> rssItems) {
        //super.onPostExecute(rssItems);

        for (int i = 0; i < rssItems.size(); i++)
            Log.i(Global.TAG, rssItems.get(i).getTitle());
    }
}
