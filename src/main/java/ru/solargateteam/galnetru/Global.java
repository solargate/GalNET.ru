package ru.solargateteam.galnetru;

public class Global {

    public static final String TAG                     = "GalNET.ru";

    public static final int NEWS_SERVICE_TASK_CODE     = 1;
    public static final int NEWS_SERVICE_STATUS_OK     = 1;
    public static final int NEWS_SERVICE_STATUS_NON    = 2;

    public static final int RADIO_SERVICE_TASK_CODE    = 2;
    public static final int RADIO_SERVICE_STATUS_START = 3;
    public static final int RADIO_SERVICE_STATUS_PREP  = 4;
    public static final int RADIO_SERVICE_STATUS_STOP  = 5;
    public static final int RADIO_SERVICE_STATUS_SOFT  = 6;
    public static final int RADIO_SERVICE_STATUS_HARD  = 7;
    public static final int RADIO_SERVICE_STATUS_NULL  = 8;

    public static final String IMAGES_DIR              = "images";

    // RSS
    public static final String RSS_TAG_ITEM            = "item";
    public static final String RSS_TAG_TITLE           = "title";
    public static final String RSS_TAG_LINK            = "link";
    public static final String RSS_TAG_DESCRIPTION     = "description";
    public static final String RSS_TAG_GUID            = "guid";
    public static final String RSS_TAG_PUBDATE         = "pubDate";

    // TODO: Переделать всю систему фидов на динамические
    public static final String RSS_FEED_GALNET_NEWS    = "http://galnet.ru/feed/1.rss";         // GalNET News
    public static final String RSS_FEED_POWERPLAY      = "http://galnet.ru/feed/4.rss";         // PowerPlay
    //public static final String RSS_FEED_WEEKLY_REPORT  = "http://galnet.ru/news/by/tag/1.rss";  // Еженедельный отчет
    public static final String RSS_FEED_COMM_GOALS     = "http://galnet.ru/news/by/tag/32.rss"; // Общественные цели
    public static final String RSS_FEED_COMM_NEWS      = "http://galnet.ru/feed/8.rss";         // Новости сообществ
    public static final String RSS_FEED_EMPIRE         = "http://galnet.ru/news/by/tag/73.rss"; // Империя
    public static final String RSS_FEED_FEDERATION     = "http://galnet.ru/news/by/tag/58.rss"; // Федерация
    public static final String RSS_FEED_ALLIANCE       = "http://galnet.ru/news/by/tag/75.rss"; // Альянс
    public static final String RSS_FEED_SITE_NEWS      = "http://galnet.ru/feed/2.rss";         // Новости ресурса

    // Feed types
    public static final String FEED_TYPE_ALL           = "ALL";
    public static final String FEED_TYPE_GALNET_NEWS   = "GALNET_NEWS";
    public static final String FEED_TYPE_POWERPLAY     = "POWERPLAY";
    //public static final String FEED_TYPE_WEEKLY_REPORT = "DAYLY_REPORT";
    public static final String FEED_TYPE_COMM_GOALS    = "COMM_GOALS";
    public static final String FEED_TYPE_COMM_NEWS     = "COMM_NEWS";
    public static final String FEED_TYPE_EMPIRE        = "EMPIRE";
    public static final String FEED_TYPE_FEDERATION    = "FEDERATION";
    public static final String FEED_TYPE_ALLIANCE      = "ALIANCE";
    public static final String FEED_TYPE_SITE_NEWS     = "SITE_NEWS";

    // Stream channels
    public static final String STREAM_SOFT             = "http://galnet.ru/soft.mp3";
    public static final String STREAM_HARD             = "http://galnet.ru/hard.mp3";

    // Fonts
    public static final String FONT_JURA_BOOK          = "JuraBook.ttf";
    public static final String FONT_JURA_BOLD          = "JuraDemiBold.ttf";
    public static final String FONT_JURA_LIGHT         = "JuraLight.ttf";
    public static final String FONT_JURA_MEDIUM        = "JuraMedium.ttf";
}
