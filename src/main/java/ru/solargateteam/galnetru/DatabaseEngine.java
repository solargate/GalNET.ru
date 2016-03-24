package ru.solargateteam.galnetru;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.solargateteam.galnetru.rss.RSSItem;

public class DatabaseEngine {

    DBHelper dbh;
    SQLiteDatabase db;

    public DatabaseEngine(Context context) {
        dbh = new DBHelper(context);
        db = dbh.getWritableDatabase();
    }

    public void close() {
        dbh.close();
    }

    private boolean checkContentExistsByLink(String link, String feedContent) {
        boolean result;
        Cursor c = db.rawQuery(String.format(DBHelper.DB_SQL_SELECTBY_LINK, link, feedContent), null);
        result = (c.getCount() != 0);
        c.close();
        return result;
    }

    /*
    private Bitmap getBitmapFromURL(String imageURL) {
        try {

            Log.d(Global.TAG, "2");

            URL url = new URL(imageURL);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap tmpBitmap = BitmapFactory.decodeStream(input);
            return tmpBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    */

    public void insertContentItem(RSSItem rssItem, String feedContent) {

        if (!checkContentExistsByLink(rssItem.getLink(), feedContent) &&
                Util.checkURL(rssItem.getLink())) {

            // TEST
            /*
            byte[] bArray = new byte[0];

            Log.d(Global.TAG, "1");

            Bitmap tmpBitmap = getBitmapFromURL("http://galnet.ru/embed/img/banner/302.png");

            Log.d(Global.TAG, "3");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            if (tmpBitmap != null) {
                tmpBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bArray = bos.toByteArray();
            }
            */

            ContentValues cv = new ContentValues();

            cv.put(DBHelper.FIELD_KEY_GUID, rssItem.getGuid());
            cv.put(DBHelper.FIELD_TITLE, rssItem.getTitle());
            cv.put(DBHelper.FIELD_LINK, rssItem.getLink());
            cv.put(DBHelper.FIELD_DESCRIPTION, rssItem.getDescription());
            cv.put(DBHelper.FIELD_PUBDATE, Util.getUnixTime(rssItem.getPubDate()));
            cv.put(DBHelper.FIELD_FEED_TYPE, feedContent);

            //cv.put(DBHelper.FIELD_IMAGE, bArray);

            db.beginTransaction();
            db.insert(DBHelper.DB_TABLE_CONTENT, null, cv);
            db.setTransactionSuccessful();
            db.endTransaction();

            Log.i(Global.TAG, "INSERT: " + rssItem.getTitle() + " " + feedContent + " " + rssItem.getLink());
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

        db = dbh.getReadableDatabase();

        Cursor c = db.query(DBHelper.DB_TABLE_CONTENT, null, selection, selectionArgs, null, null, DBHelper.FIELD_PUBDATE + " desc");

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

        dbh.close();

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
        private static final String FIELD_IMAGE       = "image";

        private static final String DB_SQL_CREATE_CONTENT = "create table " + DB_TABLE_CONTENT + " (" +
                FIELD_KEY_ID + " integer primary key autoincrement, " +
                FIELD_KEY_GUID + " text, " +
                FIELD_TITLE + " text, " +
                FIELD_LINK + " text, " +
                FIELD_DESCRIPTION + " text, " +
                FIELD_IMAGE + " blob, " +
                FIELD_FEED_TYPE + " text, " +
                FIELD_PUBDATE + " integer" +
                ");";

        private static final String DB_SQL_DROP_CONTENT = "drop table " + DB_TABLE_CONTENT + ";";

        private  static final String DB_SQL_SELECTBY_LINK = "select * from " + DB_TABLE_CONTENT +
                " where " + FIELD_LINK + " = \"%1$s\"" +
                " and " + FIELD_FEED_TYPE + " = \"%2$s\"";

        //private  static final String DB_SQL_SELECTBY_FEED_TYPE = "select * from " + DB_TABLE_CONTENT +
        //        " where " + FIELD_FEED_TYPE + " = \"%1$s\"";

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
