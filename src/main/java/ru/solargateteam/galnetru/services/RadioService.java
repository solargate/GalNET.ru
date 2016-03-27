package ru.solargateteam.galnetru.services;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import ru.solargateteam.galnetru.Global;

public class RadioService extends IntentService implements MediaPlayer.OnPreparedListener {

    public static final String ACTION_SOFT_PLAY = "ru.solargateteam.galnetru.action.SOFT_PLAY";

    MediaPlayer mediaPlayer;

    public RadioService() {
        super("RadioService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(Global.TAG, "RadioService onHandleIntent");

        final String action = intent.getAction();

        if (ACTION_SOFT_PLAY.equals(action)) {

            Log.i(Global.TAG, "RadioService onStartCommand start");

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

            mediaPlayer.setOnPreparedListener(this);

            try {

                Log.i(Global.TAG, "RadioService onHandleIntent ACTION_SOFT_PLAY");

                mediaPlayer.setDataSource(Global.STREAM_SOFT);
                mediaPlayer.prepareAsync();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

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

    @Override
    public void onPrepared(MediaPlayer mp) {

        Log.i(Global.TAG, "RadioService onPrepared");

        mp.start();
    }
}
