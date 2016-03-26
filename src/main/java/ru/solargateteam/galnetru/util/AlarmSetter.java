package ru.solargateteam.galnetru.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import ru.solargateteam.galnetru.Global;
import ru.solargateteam.galnetru.pref.PrefEngine;
import ru.solargateteam.galnetru.receivers.AlarmReceiver;

public class AlarmSetter {

    public AlarmSetter() {
    }

    public void setRefreshAlarm(Context context) {

        AlarmManager alarmManager;
        PendingIntent alarmIntent;

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentToFire = new Intent(AlarmReceiver.ACTION_REFRESH_GALNETRU_ALARM);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intentToFire, 0);

        PrefEngine pe = new PrefEngine(context);

        int refreshInterval = pe.getRefreshInterval();

        Log.i(Global.TAG, "RefreshInterval: " + String.valueOf(refreshInterval));

        int alarmType = AlarmManager.ELAPSED_REALTIME;

        long timeToRefresh = SystemClock.elapsedRealtime() + refreshInterval * 60 * 1000;

        alarmManager.setInexactRepeating(alarmType, timeToRefresh, refreshInterval * 60 * 1000, alarmIntent);

    }
}
