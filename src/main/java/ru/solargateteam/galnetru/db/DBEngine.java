package ru.solargateteam.galnetru.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.solargateteam.galnetru.Global;
import ru.solargateteam.galnetru.util.Util;
import ru.solargateteam.galnetru.rss.RSSItem;

public class DBEngine {

    DBHelper dbh;
    SQLiteDatabase db;

    public DBEngine(Context context) {
        dbh = new DBHelper(context);
        db = dbh.getWritableDatabase();
    }

    public void close() {
        Log.d(Global.TAG, "DB Close");
        dbh.close();
    }

    private boolean checkContentExistsByLink(String link, String feedContent) {
        boolean result;
        Cursor c = db.rawQuery(String.format(DBHelper.DB_SQL_SELECTBY_LINK, link, feedContent), null);
        result = (c.getCount() != 0);
        c.close();
        return result;
    }

    public void insertContentItem(RSSItem rssItem, String feedContent) {

        if (!checkContentExistsByLink(rssItem.getLink(), feedContent) &&
                Util.checkURL(rssItem.getLink())) {

            ContentValues cv = new ContentValues();

            cv.put(DBHelper.FIELD_KEY_GUID, rssItem.getGuid());
            cv.put(DBHelper.FIELD_TITLE, rssItem.getTitle());
            cv.put(DBHelper.FIELD_LINK, rssItem.getLink());
            cv.put(DBHelper.FIELD_DESCRIPTION, rssItem.getDescription());
            cv.put(DBHelper.FIELD_PUBDATE, Util.getUnixTime(rssItem.getPubDate()));
            cv.put(DBHelper.FIELD_FEED_TYPE, feedContent);
            cv.put(DBHelper.FIELD_NEW_POST, DBHelper.NEW_POST_TRUE);

            db.beginTransaction();
            db.insert(DBHelper.DB_TABLE_CONTENT, null, cv);
            db.setTransactionSuccessful();
            db.endTransaction();

            Log.i(Global.TAG, "INSERT: " + rssItem.getTitle() + " " + feedContent + " " + rssItem.getLink());
        }
    }

    public void updateImage(DBItem dbItem) {
        ContentValues cv = new ContentValues();

        cv.put(DBHelper.FIELD_IMAGE_PATH, dbItem.getImagePath());

        db.beginTransaction();
        db.update(DBHelper.DB_TABLE_CONTENT, cv, DBHelper.FIELD_LINK + " = ?", new String[]{dbItem.getLink()});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void updateNewPostToOld() {
        ContentValues cv = new ContentValues();

        cv.put(DBHelper.FIELD_NEW_POST, DBHelper.NEW_POST_FALSE);

        db.beginTransaction();
        db.update(DBHelper.DB_TABLE_CONTENT, cv, DBHelper.FIELD_NEW_POST + " = ?", new String[]{Integer.toString(DBHelper.NEW_POST_TRUE)});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public List<DBItem> readContent(String feedType) {
        List<DBItem> returnList = new ArrayList<DBItem>();
        String selection = null;
        String[] selectionArgs = null;

        if (feedType.equals(Global.FEED_TYPE_ALL)) {
            selection = null;
            selectionArgs = null;
        } else {
            selection = DBHelper.FIELD_FEED_TYPE + " = ?";
            selectionArgs = new String[] { feedType };
        }

        Cursor c = db.query(DBHelper.DB_TABLE_CONTENT, null, selection, selectionArgs, null, null, DBHelper.FIELD_PUBDATE + " desc");

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    DBItem currentItem = new DBItem();
                    currentItem.setId(c.getInt(c.getColumnIndex(DBHelper.FIELD_KEY_ID)));
                    currentItem.setTitle(c.getString(c.getColumnIndex(DBHelper.FIELD_TITLE)));
                    currentItem.setLink(c.getString(c.getColumnIndex(DBHelper.FIELD_LINK)));
                    currentItem.setDescription(c.getString(c.getColumnIndex(DBHelper.FIELD_DESCRIPTION)));
                    currentItem.setImagePath(c.getString(c.getColumnIndex(DBHelper.FIELD_IMAGE_PATH)));
                    currentItem.setNewPost(c.getInt(c.getColumnIndex(DBHelper.FIELD_NEW_POST)));
                    returnList.add(currentItem);
                } while (c.moveToNext());
            }
            c.close();
        }
        return returnList;
    }

    public List<DBItem> readContentNew() {
        List<DBItem> returnList = new ArrayList<DBItem>();

        Cursor c = db.query(DBHelper.DB_TABLE_CONTENT, null, DBHelper.FIELD_NEW_POST + " = ?", new String[]{Integer.toString(DBHelper.NEW_POST_TRUE)}, null, null, DBHelper.FIELD_PUBDATE + " desc");

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    DBItem currentItem = new DBItem();
                    currentItem.setId(c.getInt(c.getColumnIndex(DBHelper.FIELD_KEY_ID)));
                    currentItem.setTitle(c.getString(c.getColumnIndex(DBHelper.FIELD_TITLE)));
                    currentItem.setLink(c.getString(c.getColumnIndex(DBHelper.FIELD_LINK)));
                    currentItem.setDescription(c.getString(c.getColumnIndex(DBHelper.FIELD_DESCRIPTION)));
                    currentItem.setImagePath(c.getString(c.getColumnIndex(DBHelper.FIELD_IMAGE_PATH)));
                    currentItem.setNewPost(c.getInt(c.getColumnIndex(DBHelper.FIELD_NEW_POST)));
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
        private static final String FIELD_IMAGE_PATH  = "imagepath";
        private static final String FIELD_NEW_POST    = "newpost";

        private static final int NEW_POST_TRUE  = 1;
        private static final int NEW_POST_FALSE = 0;

        private static final String DB_SQL_CREATE_CONTENT = "create table " + DB_TABLE_CONTENT + " (" +
                FIELD_KEY_ID + " integer primary key autoincrement, " +
                FIELD_KEY_GUID + " text, " +
                FIELD_TITLE + " text, " +
                FIELD_LINK + " text, " +
                FIELD_DESCRIPTION + " text, " +
                FIELD_IMAGE_PATH + " text, " +
                FIELD_FEED_TYPE + " text, " +
                FIELD_PUBDATE + " integer, " +
                FIELD_NEW_POST + " integer" +
                ");";

        private static final String DB_SQL_DROP_CONTENT = "drop table " + DB_TABLE_CONTENT + ";";

        private  static final String DB_SQL_SELECTBY_LINK = "select * from " + DB_TABLE_CONTENT +
                " where " + FIELD_LINK + " = \"%1$s\"" +
                " and " + FIELD_FEED_TYPE + " = \"%2$s\"";

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
