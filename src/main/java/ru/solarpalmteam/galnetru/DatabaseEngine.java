package ru.solarpalmteam.galnetru;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.solarpalmteam.galnetru.rss.RSSItem;

public class DatabaseEngine {

    DBHelper dbh;
    SQLiteDatabase db;

    public DatabaseEngine(Context context) {
        dbh = new DBHelper(context);
        db = dbh.getWritableDatabase();
    }

    private boolean checkContentExistsByLink(String link) {
        boolean result;
        Cursor c = db.rawQuery(String.format(DBHelper.DB_SQL_SELECTBY_LINK, link), null);
        result = (c.getCount() != 0);
        c.close();
        return result;
    }

    public void insertContentItem(RSSItem rssItem, String feedContent) {
        if (!checkContentExistsByLink(rssItem.getLink())) {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.FIELD_KEY_GUID, rssItem.getGuid());
            cv.put(DBHelper.FIELD_TITLE, rssItem.getTitle());
            cv.put(DBHelper.FIELD_LINK, rssItem.getLink());
            cv.put(DBHelper.FIELD_DESCRIPTION, rssItem.getDescription());
            // TODO: Добавить pubdate
            cv.put(DBHelper.FIELD_FEED_TYPE, feedContent);
            db.insert(DBHelper.DB_TABLE_CONTENT, null, cv);
            Log.i(Global.TAG, "INSERT: " + rssItem.getTitle());
        }
    }

    public List<RSSItem> readContent(String feedType) {
        List<RSSItem> returnList = new ArrayList<RSSItem>();
        String selection = null;
        String[] selectionArgs = null;

        if (feedType.equals(Global.FEED_TYPE_ALL)) {
            selection = null;
            selectionArgs = null;
        } else {
            selection = DBHelper.FIELD_FEED_TYPE + " = ?";
            selectionArgs = new String[] { feedType };
        }

        Cursor c = db.query(DBHelper.DB_TABLE_CONTENT, null, selection, selectionArgs, null, null, DBHelper.FIELD_PUBDATE);

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    RSSItem currentItem = new RSSItem();
                    currentItem.setTitle(c.getString(c.getColumnIndex(DBHelper.FIELD_TITLE)));
                    currentItem.setLink(c.getString(c.getColumnIndex(DBHelper.FIELD_LINK)));
                    currentItem.setDescription(c.getString(c.getColumnIndex(DBHelper.FIELD_DESCRIPTION)));
                    returnList.add(currentItem);
                } while (c.moveToNext());
            }
            c.close();
        }
        return returnList;
    }

    private class DBHelper extends SQLiteOpenHelper {

        private static final int    DB_VERSION        = 1;
        private static final String DB_NAME           = "GalNET.ru.db";
        private static final String DB_TABLE_CONTENT  = "Content";

        private static final String FIELD_KEY_ID      = "id";
        private static final String FIELD_KEY_GUID    = "guid";
        private static final String FIELD_TITLE       = "title";
        private static final String FIELD_LINK        = "link";
        private static final String FIELD_DESCRIPTION = "description";
        private static final String FIELD_PUBDATE     = "pubdate";
        private static final String FIELD_FEED_TYPE   = "feedtype";

        private static final String DB_SQL_CREATE_CONTENT = "create table " + DB_TABLE_CONTENT + " (" +
                FIELD_KEY_ID + " integer primary key autoincrement, " +
                FIELD_KEY_GUID + " text, " +
                FIELD_TITLE + " text, " +
                FIELD_LINK + " text, " +
                FIELD_DESCRIPTION + " text, " +
                FIELD_FEED_TYPE + " text, " +
                FIELD_PUBDATE + " integer" +
                ");";

        private static final String DB_SQL_DROP_CONTENT = "drop table " + DB_TABLE_CONTENT + ";";

        private  static final String DB_SQL_SELECTBY_LINK = "select * from " + DB_TABLE_CONTENT +
                " where " + FIELD_LINK + " = \"%1$s\"";

        private  static final String DB_SQL_SELECTBY_FEED_TYPE = "select * from " + DB_TABLE_CONTENT +
                " where " + FIELD_FEED_TYPE + " = \"%1$s\"";

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(Global.TAG, "Creating DB...");
            Log.i(Global.TAG, DB_SQL_CREATE_CONTENT);
            db.execSQL(DB_SQL_CREATE_CONTENT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(Global.TAG, "Upgrading DB...");
            db.execSQL(DB_SQL_DROP_CONTENT);
            db.execSQL(DB_SQL_CREATE_CONTENT);
        }
    }
}
