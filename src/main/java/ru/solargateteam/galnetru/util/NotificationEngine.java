package ru.solargateteam.galnetru.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.List;

import ru.solargateteam.galnetru.MainActivity;
import ru.solargateteam.galnetru.R;
import ru.solargateteam.galnetru.db.DBEngine;
import ru.solargateteam.galnetru.db.DBItem;
import ru.solargateteam.galnetru.pref.PrefEngine;

import static java.lang.String.format;

public class NotificationEngine {

    public static final int NOTIFICATION_ID_NEW_POST = 1;
    public static final int NOTIFICATION_ID_RADIO    = 2;

    NotificationManager nm;
    DBEngine dbe;

    public NotificationEngine(Context context) {
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        dbe = new DBEngine(context);
    }

    public void processNotificationNewPost(Context context) {
        PrefEngine pe = new PrefEngine(context);
        if (pe.isNotificationEnabled())
            showNotificationNewPost(context);
        else
            removeNotificationNewPost();
    }

    /*
    public void processNotificationRadio(Context context, String radioName) {
        PrefEngine pe = new PrefEngine(context);
        showNotificationRadio(context, radioName);
    }
    */

    private void showNotificationNewPost(Context context) {

        List<DBItem> listContent = dbe.readContentNew();
        String text;

        if (listContent.size() > 0) {

            if (listContent.size() > 1) {
                text = Integer.toString(listContent.size()) + " " + context.getString(R.string.notification_new_post_text);
            } else {
                text = listContent.get(0).getTitle();
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            builder.setSmallIcon(R.drawable.ic_notify)
                    .setContentTitle(String.valueOf(context.getString(R.string.notification_new_post_title)))
                    .setContentText(String.valueOf(text))
                    .setDefaults(Notification.DEFAULT_SOUND /*| Notification.DEFAULT_LIGHTS*/)
                    .setLights(Color.YELLOW, 0, 1)
                    .setAutoCancel(true);

            Intent resultIntent = new Intent(context, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);

            nm.notify(NOTIFICATION_ID_NEW_POST, builder.build());
        }
        dbe.close();
    }

    private void removeNotificationNewPost() {
        nm.cancel(NOTIFICATION_ID_NEW_POST);
    }

    /*
    private void showNotificationRadio(Context context, String radioName) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.drawable.ic_notify)
                .setContentTitle(String.valueOf(context.getString(R.string.notification_radio_title)))
                .setContentText(format(String.valueOf(context.getString(R.string.notification_radio_text)), radioName))
                        .setOngoing(true)
                        .setShowWhen(false);

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        nm.notify(NOTIFICATION_ID_RADIO, builder.build());

    }

    public void removeNotificationRadio() {
        nm.cancel(NOTIFICATION_ID_RADIO);
    }
    */

    public Notification buildRadioNotification(Context context, String radioName) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle(String.valueOf(context.getString(R.string.notification_radio_title)))
                .setContentText(format(String.valueOf(context.getString(R.string.notification_radio_text)), radioName))
                .setOngoing(true)
                .setShowWhen(false);

        if (radioName.equals("SOFT")) {
            builder.setSmallIcon(R.drawable.ic_notify_radio_soft);
        } else if (radioName.equals("HARD")) {
            builder.setSmallIcon(R.drawable.ic_notify_radio_hard);
        }

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        return builder.build();
    }

}
