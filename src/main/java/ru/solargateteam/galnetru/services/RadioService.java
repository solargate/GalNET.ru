package ru.solargateteam.galnetru.services;

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

    public static final String ACTION_SOFT_PLAY = "ru.solargateteam.galnetru.action.SOFT_PLAY";
    public static final String ACTION_HARD_PLAY = "ru.solargateteam.galnetru.action.HARD_PLAY";

    MediaPlayer mediaPlayer;
    String playerStatus;
    NotificationEngine ne;

    public RadioService() {
        this.playerStatus = "";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(Global.TAG, "RadioService onStartCommand");

        ne = new NotificationEngine(getApplicationContext());

        String action = intent.getAction();

        if (playerStatus.equals(action)) {
            releaseMediaPlayer();
        } else {

            releaseMediaPlayer();

            playerStatus = action;

            try {

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

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(Global.TAG, "RadioService onDestroy");

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        releaseMediaPlayer();
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

        mp.start();
    }

    private void releaseMediaPlayer() {

        Log.d(Global.TAG, "RadioService releaseMediaPlayer");

        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
                playerStatus = "";
                ne.removeNotificationRadio();
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
