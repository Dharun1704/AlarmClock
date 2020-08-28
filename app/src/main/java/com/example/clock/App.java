package com.example.clock;

import android.app.AlertDialog;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        createNotification();
    }

    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "Timer", "Timer done!",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Timer has completed");
            NotificationManager manager =   getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

        }
    }
}
