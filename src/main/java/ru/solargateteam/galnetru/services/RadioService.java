package ru.solargateteam.galnetru.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import ru.solargateteam.galnetru.Global;
import ru.solargateteam.galnetru.util.NotificationEngine;

public class RadioService extends Service implements MediaPlayer.OnPreparedListener {

    public static final String ACTION_SOFT_PLAY            = "ru.solargateteam.galnetru.action.SOFT_PLAY";
    public static final String ACTION_HARD_PLAY            = "ru.solargateteam.galnetru.action.HARD_PLAY";
    public static final String ACTION_CHECK_STATUS         = "ru.solargateteam.galnetru.action.CHECK_STATUS";
    public final static String PARAM_PINTENT_FROM_ACTIVITY = "ru.solargateteam.galnetru.pendingintent.RADIO_FROM_ACTIVITY";

    MediaPlayer mediaPlayer;
    String playerStatus;
    NotificationEngine ne;

    PendingIntent pi;

    public RadioService() {
        this.playerStatus = "";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(Global.TAG, "RadioService onStartCommand");

        ne = new NotificationEngine(getApplicationContext());

        if (intent != null) {

            //ne = new NotificationEngine(getApplicationContext());

            String action = intent.getAction();
            pi = intent.getParcelableExtra(PARAM_PINTENT_FROM_ACTIVITY);

            if (action.equals(ACTION_CHECK_STATUS)) {
                try {
                    if (playerStatus.equals(ACTION_SOFT_PLAY)) {
                        pi.send(Global.RADIO_SERVICE_STATUS_SOFT);
                    } else if (playerStatus.equals(ACTION_HARD_PLAY)) {
                        pi.send(Global.RADIO_SERVICE_STATUS_HARD);
                    } else if (playerStatus == null || playerStatus.equals("")) {
                        pi.send(Global.RADIO_SERVICE_STATUS_NULL);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (playerStatus.equals(action)) {
                    releaseMediaPlayer();
                } else {

                    releaseMediaPlayer();

                    playerStatus = action;

                    try {

                        pi.send(Global.RADIO_SERVICE_STATUS_START);

                        mediaPlayer = new MediaPlayer();

                        if (ACTION_SOFT_PLAY.equals(action)) {
                            mediaPlayer.setDataSource(Global.STREAM_SOFT);
                        } else if (ACTION_HARD_PLAY.equals(action)) {
                            mediaPlayer.setDataSource(Global.STREAM_HARD);
                        }

                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                        Log.d(Global.TAG, "prepareAsync");
                        mediaPlayer.setOnPreparedListener(this);
                        mediaPlayer.prepareAsync();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            Log.d(Global.TAG, "RadioService stopSelf");
            stopSelf(startId);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(Global.TAG, "RadioService onDestroy");

        if (ne != null)
            ne.removeNotificationRadio();

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            releaseMediaPlayer();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        Log.d(Global.TAG, "RadioService onPrepared");

        ne.processNotificationRadio(getApplicationContext(), getRadioName());

        try {
            mp.start();
            pi.send(Global.RADIO_SERVICE_STATUS_PREP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void releaseMediaPlayer() {

        Log.d(Global.TAG, "RadioService releaseMediaPlayer");

        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
                playerStatus = "";
                ne.removeNotificationRadio();
                pi.send(Global.RADIO_SERVICE_STATUS_STOP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getRadioName() {
        if (playerStatus.equals(ACTION_SOFT_PLAY)) {
            return "SOFT";
        } else if (playerStatus.equals(ACTION_HARD_PLAY)) {
            return "HARD";
        }
        return "";
    }
}
