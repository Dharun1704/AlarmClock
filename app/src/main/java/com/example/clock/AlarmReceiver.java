package com.example.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("RingtoneName");
        MediaPlayer mediaPlayer = new MediaPlayer();

        assert name != null;
        switch (name) {
            case "Mr.Bean theme":
                mediaPlayer = MediaPlayer.create(context, R.raw.bean_song);
                break;
            case "Giorno theme":
                mediaPlayer = MediaPlayer.create(context, R.raw.giorno_theme);
                break;
            case "Pirates of the Caribbean theme":
                mediaPlayer = MediaPlayer.create(context, R.raw.pirates_ot_caribbean);
                break;
            case "Tom & Jerry theme":
                mediaPlayer = MediaPlayer.create(context, R.raw.tomjerry_theme);
                break;
            case "The good doctor theme":
                mediaPlayer = MediaPlayer.create(context, R.raw.the_good_doctor);
                break;
        }
        mediaPlayer.start();
    }
}
