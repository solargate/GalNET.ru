package ru.solargateteam.galnetru.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ru.solargateteam.galnetru.Global;
import ru.solargateteam.galnetru.services.RSSService;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String ACTION_REFRESH_GALNETRU_ALARM = "ru.solargateteam.galnetru.ACTION_REFRESH_GALNETRU_ALARM";

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(Global.TAG, "Starting service on alarm");

        Intent intentService;

        intentService = new Intent(context, RSSService.class);
        intentService.setAction(RSSService.ACTION_FROM_ALARM);

        context.startService(intentService);
    }
}
