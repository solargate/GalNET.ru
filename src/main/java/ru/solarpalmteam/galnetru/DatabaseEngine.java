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

    private static final int    DB_VERSION        = 1;
    private static final String DB_NAME           = "GalNET.ru.db";
    private static final String DB_TABLE_CONTENT  = "Content";

    private static final String FIELD_KEY_ID      = "id";
    private static final String FIELD_KEY_GUID    = "guid";
    private static final String FIELD_TITLE       = "title";
    private static final String FIELD_LINK        = "link";
    private static final String FIELD_DESCRIPTION = "description";
    private static final String FIELD_PUBDATE     = "pubdate";

    DBHelper dbh;
    SQLiteDatabase db;

    public DatabaseEngine(Context context) {
        dbh = new DBHelper(context);
        db = dbh.getWritableDatabase();
    }

    public void insertContentItem(RSSItem rssItem) {
        ContentValues  cv = new ContentValues();
        cv.put(FIELD_KEY_GUID, rssItem.getGuid());
        cv.put(FIELD_TITLE, rssItem.getTitle());

        Log.d(Global.TAG, "INSERT TITLE: " + rssItem.getTitle());

        cv.put(FIELD_LINK, rssItem.getLink());
        cv.put(FIELD_DESCRIPTION, rssItem.getDescription());
        // TODO: Добавить pubdate
        db.insert(DB_TABLE_CONTENT, null, cv);
    }

    public List<RSSItem> readContentAll() {
        //RSSItem currentItem = new RSSItem();
        List<RSSItem> returnList = new ArrayList<RSSItem>();

        Cursor c = db.query(DB_TABLE_CONTENT, null, null, null, null, null, null);

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    RSSItem currentItem = new RSSItem();

                    currentItem.setTitle(c.getString(c.getColumnIndex(FIELD_TITLE)));
                    currentItem.setLink(c.getString(c.getColumnIndex(FIELD_LINK)));
                    currentItem.setDescription(c.getString(c.getColumnIndex(FIELD_DESCRIPTION)));

                    Log.d(Global.TAG, "currentItem TITLE: " + currentItem.getTitle());

                    returnList.add(currentItem);
                } while (c.moveToNext());
            }
            c.close();
        }

        //Log.d(Global.TAG, "!!! returnList: " + returnList);
        for (int i = 0; i < returnList.size(); i++)
            Log.d(Global.TAG, "!!! returnList: " + returnList.get(i).getTitle());

        return returnList;
    }

    private class DBHelper extends SQLiteOpenHelper {

        private static final String DB_SQL_CREATE_CONTENT = "create table " + DB_TABLE_CONTENT + " (" +
                FIELD_KEY_ID + " integer primary key autoincrement, " +
                FIELD_KEY_GUID + " text, " +
                FIELD_TITLE + " text, " +
                FIELD_LINK + " text, " +
                FIELD_DESCRIPTION + " text, " +
                FIELD_PUBDATE + " integer" +
                ");";

        private static final String DB_SQL_DROP_CONTENT = "drop table " + DB_TABLE_CONTENT + ";";

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
