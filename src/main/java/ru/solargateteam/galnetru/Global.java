package ru.solargateteam.galnetru;

/*
TODO: Список задач:
- PostActivity во Fragment
- PrefActivity во Fragment
- Кнопка "Back" в связи с пунктами выше (анимированная). Показ фрагментов тоже анимированный
- При чтении даты из RSS обрабатывать ее как Description
- Добавить дату в CardView и в посты
- Добавить скроллбар в RecyclerView
- Пуржинг базы и картинок от старых постов
- Уведомление о проигрывании радио
- Буфферинг при старте плеера
- Попробовать читать теги mp3 и выводить название песни
- Кнопки радио в квадратные рамки. При нажатии инвертировать
- Тема оформления в Navigation View
- Тема оформления в Toolbar
- About dialog
- Иконка приложения
- Иконки уведомлений
 */

public class Global {

    public static final String TAG                     = "GalNET.ru";

    public static final int NEWS_SERVICE_TASK_CODE     = 1;
    public static final int NEWS_SERVICE_STATUS_OK     = 1;
    public static final int NEWS_SERVICE_STATUS_NON    = 2;

    public static final String IMAGES_DIR              = "images";

    // RSS
    public static final String RSS_TAG_ITEM            = "item";
    public static final String RSS_TAG_TITLE           = "title";
    public static final String RSS_TAG_LINK            = "link";
    public static final String RSS_TAG_DESCRIPTION     = "description";
    public static final String RSS_TAG_GUID            = "guid";
    public static final String RSS_TAG_PUBDATE         = "pubDate";

    public static final String RSS_FEED_GALNET_NEWS    = "http://galnet.ru/feed/1.rss";         // GalNET News
    public static final String RSS_FEED_POWERPLAY      = "http://galnet.ru/feed/4.rss";         // PowerPlay
    public static final String RSS_FEED_WEEKLY_REPORT  = "http://galnet.ru/news/by/tag/1.rss";  // Еженедельный отчет
    public static final String RSS_FEED_COMM_GOALS     = "http://galnet.ru/news/by/tag/32.rss"; // Общественные цели
    public static final String RSS_FEED_COMM_NEWS      = "http://galnet.ru/feed/8.rss";         // Новости сообществ
    public static final String RSS_FEED_SITE_NEWS      = "http://galnet.ru/feed/2.rss";         // Новости ресурса

    // Feed types
    public static final String FEED_TYPE_ALL           = "ALL";
    public static final String FEED_TYPE_GALNET_NEWS   = "GALNET_NEWS";
    public static final String FEED_TYPE_POWERPLAY     = "POWERPLAY";
    public static final String FEED_TYPE_WEEKLY_REPORT = "DAYLY_REPORT";
    public static final String FEED_TYPE_COMM_GOALS    = "COMM_GOALS";
    public static final String FEED_TYPE_COMM_NEWS     = "COMM_NEWS";
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
