package ru.solarpalmteam.galnetru;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseEngine {

    private DBHelper dbh;

    public DatabaseEngine(Context context) {
        dbh = new DBHelper(context);
        dbh.getWritableDatabase();
    }

    private static class DBHelper extends SQLiteOpenHelper {

        private static final int    DB_VERSION        = 1;
        private static final String DB_NAME           = "GalNET.ru.db";
        private static final String DB_TABLE_CONTENT  = "Content";

        private static final String FIELD_KEY_GUID    = "guid";
        private static final String FIELD_TITLE       = "title";
        private static final String FIELD_LINK        = "link";
        private static final String FIELD_DESCRIPTION = "description";
        private static final String FIELD_PUBDATE     = "pubdate";

        private static final String DB_SQL_CREATE_CONTENT = "create table " + DB_TABLE_CONTENT + " (" +
                FIELD_KEY_GUID + " text primary key, " +
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
