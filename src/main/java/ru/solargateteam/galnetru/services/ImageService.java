package ru.solargateteam.galnetru.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import ru.solargateteam.galnetru.Global;

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
}
