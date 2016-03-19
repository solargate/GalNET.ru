package ru.solargateteam.galnetru.rss;

import android.content.Context;

import ru.solargateteam.galnetru.Global;

// Головной класс для RSS, разруливает задачи
public class RSSMaster {

    RSSProcessTask task_GALNET_NEWS;
    RSSProcessTask task_POWERPLAY;
    RSSProcessTask task_DAYLY_REPORT;
    RSSProcessTask task_COMM_GOALS;
    RSSProcessTask task_SITE_NEWS;

    public RSSMaster(Context context) {
        task_GALNET_NEWS = new RSSProcessTask();
        task_GALNET_NEWS.setContext(context);

        task_POWERPLAY = new RSSProcessTask();
        task_POWERPLAY.setContext(context);

        task_DAYLY_REPORT = new RSSProcessTask();
        task_DAYLY_REPORT.setContext(context);

        task_COMM_GOALS = new RSSProcessTask();
        task_COMM_GOALS.setContext(context);

        task_SITE_NEWS = new RSSProcessTask();
        task_SITE_NEWS.setContext(context);
    }

    public void startReadingRSS() {
        task_GALNET_NEWS.setFeedType(Global.FEED_TYPE_GALNET_NEWS);
        task_GALNET_NEWS.execute(Global.RSS_FEED_GALNET_NEWS);

        task_POWERPLAY.setFeedType(Global.FEED_TYPE_POWERPLAY);
        task_POWERPLAY.execute(Global.RSS_FEED_POWERPLAY);

        task_DAYLY_REPORT.setFeedType(Global.FEED_TYPE_WEEKLY_REPORT);
        task_DAYLY_REPORT.execute(Global.RSS_FEED_WEEKLY_REPORT);

        task_COMM_GOALS.setFeedType(Global.FEED_TYPE_COMM_GOALS);
        task_COMM_GOALS.execute(Global.RSS_FEED_COMM_GOALS);

        task_SITE_NEWS.setFeedType(Global.FEED_TYPE_SITE_NEWS);
        task_SITE_NEWS.execute(Global.RSS_FEED_SITE_NEWS);
    }

}
