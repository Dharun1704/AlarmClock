package com.example.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent Alarm = new Intent(context, AlarmScreen.class);
        Alarm.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Alarm);
    }
}
