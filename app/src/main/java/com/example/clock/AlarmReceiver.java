package com.example.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class AlarmReceiver extends BroadcastReceiver {

    public MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("alarmRingtone");
        mediaPlayer = new MediaPlayer();

        assert name != null;
        switch (name) {
            case "Mr.Bean theme":
                mediaPlayer = MediaPlayer.create(context, R.raw.bean_song);
                mediaPlayer.start();
                break;
            case "Giorno theme":
                mediaPlayer = MediaPlayer.create(context, R.raw.giorno_theme);
                mediaPlayer.start();
                break;
            case "Pirates of the Caribbean theme":
                mediaPlayer = MediaPlayer.create(context, R.raw.pirates_ot_caribbean);
                mediaPlayer.start();
                break;
            case "Null":
                mediaPlayer.stop();
        }
    }
}
