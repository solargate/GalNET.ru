package ru.solargateteam.galnetru.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ru.solargateteam.galnetru.Global;
import ru.solargateteam.galnetru.services.RSSService;

public class BootReceiver extends BroadcastReceiver {

    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(Global.TAG, "Starting service on boot");

        Intent intentService;

        intentService = new Intent(context, RSSService.class);
        intentService.setAction(RSSService.ACTION_FROM_BOOT);

        context.startService(intentService);
    }
}
