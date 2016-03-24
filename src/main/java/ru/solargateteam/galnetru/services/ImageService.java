package ru.solargateteam.galnetru.services;

import android.app.IntentService;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ru.solargateteam.galnetru.Global;
import ru.solargateteam.galnetru.db.DBEngine;
import ru.solargateteam.galnetru.db.DBItem;

public class ImageService extends IntentService {

    public ImageService() {
        super("ImageService");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Global.TAG, "ImageService onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Global.TAG, "ImageService onDestroy");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(Global.TAG, "ImageService onHandleIntent");

        processImages();

        /*
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
        */
    }

    private Bitmap getBitmapFromURL(String imageURL) { // http://galnet.ru/embed/img/banner/302.png
        try {
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

    /*
    private byte[] loadBitmap(String imageURL) { // http://galnet.ru/embed/img/banner/302.png

        byte[] bArray = new byte[0];

        Bitmap tmpBitmap = getBitmapFromURL(imageURL);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (tmpBitmap != null) {
            tmpBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bArray = bos.toByteArray();
        }

        if (bArray != null)
            Log.d(Global.TAG, "bArray length: " + bArray.length);

        return bArray;
    }
    */

    private void processImages() {

        DBEngine dbe = new DBEngine(getApplicationContext());

        List<DBItem> itemsList = dbe.readContent(Global.FEED_TYPE_ALL);

        for (DBItem item : itemsList) {
            if (item.getImagePath() == null) {
                //Log.i(Global.TAG, "TTTTTTTTTTTTTTTTTTTTTTTT: " + item.getTitle());

                //item.setImagePath(loadBitmap("http://galnet.ru/embed/img/banner/302.png"));

                Bitmap image = getBitmapFromURL("http://galnet.ru/embed/img/banner/302.png");

                if (image != null) {

                    ContextWrapper cw = new ContextWrapper(getApplicationContext());
                    File dir = cw.getDir(Global.IMAGES_DIR, Context.MODE_PRIVATE);
                    File path = new File(dir, item.getId() + ".png");
                    FileOutputStream fos = null;

                    try {
                        fos = new FileOutputStream(path);
                        image.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    Log.d(Global.TAG, "IMAGE PATH: " + dir.getAbsolutePath());

                    item.setImagePath(dir.getAbsolutePath());

                    dbe.updateImage(item);
                }

                //dbe.updateImage(item);
            }
        }

        dbe.close();
    }
}
