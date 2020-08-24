package com.example.clock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements SetAlarmSheet.AlarmSetListener, NavigationView.OnNavigationItemSelectedListener{

    private LinearLayout TimeLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private TextView noAlarmFound, alarmTime, alarmPeriod, alarmDays;
    private Button alarmCancel;
    private ImageButton btnAlarmAdd;

    int almHour = 0, almMinute = 0;
    String almPeriod = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#252525"));
        window.setNavigationBarColor(Color.parseColor("#252525"));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#252525"));

        drawerLayout = findViewById(R.id.alarmDrawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        TimeLayout = findViewById(R.id.timeLayout);

        alarmTime = findViewById(R.id.alarm_time);
        alarmPeriod = findViewById(R.id.alarm_period);
        alarmDays = findViewById(R.id.alarm_days);
        alarmCancel = findViewById(R.id.alarm_cancel);
        noAlarmFound = findViewById(R.id.noAlarmFound);
        btnAlarmAdd = findViewById(R.id.btnAdd);

        btnAlarmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetAlarmSheet sheet = new SetAlarmSheet();
                sheet.show(getSupportFragmentManager(), "AlarmSheet");
            }
        });

        checkAlarm();

        alarmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();

                almHour = 0;
                SharedPreferences sHour = getSharedPreferences("AlarmHour", MODE_PRIVATE);
                SharedPreferences.Editor editorH = sHour.edit();
                editorH.putInt("almHour", almHour);
                editorH.apply();

                almMinute = 0;
                SharedPreferences sMinute = getSharedPreferences("AlarmMinute", MODE_PRIVATE);
                SharedPreferences.Editor editor = sMinute.edit();
                editor.putInt("almMinute", almMinute);
                editor.apply();

                almPeriod = null;

                checkAlarm();
            }
        });

    }

    @Override
    public void onSet(int hour, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        SharedPreferences sHour = getSharedPreferences("AlarmHour", MODE_PRIVATE);
        SharedPreferences.Editor editorH = sHour.edit();
        editorH.putInt("almHour", hour);
        editorH.apply();

        if (hour > 12) {
            almHour = hour - 12;
            almPeriod = "PM";
        }
        else {
            almHour = hour;
            almPeriod = "AM";
        }

        almMinute = minute;

        SharedPreferences sMinute = getSharedPreferences("AlarmMinute", MODE_PRIVATE);
        SharedPreferences.Editor editor = sMinute.edit();
        editor.putInt("almMinute", almMinute);
        editor.apply();


        alarmTime.setText(String.format("%02d", almHour) + ":" + String.format("%02d", almMinute));
        alarmPeriod.setText(almPeriod);
        checkAlarm();
        startAlarm(c);
    }

    private void checkAlarm() {

        SharedPreferences sHour = getSharedPreferences("AlarmHour", MODE_PRIVATE);
        almHour = sHour.getInt("almHour", 0);
        SharedPreferences sMinute = getSharedPreferences("AlarmMinute", MODE_PRIVATE);
        almMinute = sMinute.getInt("almMinute", 0);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, almHour);
        c.set(Calendar.MINUTE, almMinute);
        c.set(Calendar.SECOND, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
            alarmDays.setText("Tomorrow");
        }

        if (almHour == 0 && almMinute == 0 && almPeriod == null) {
            noAlarmFound.setVisibility(View.VISIBLE);
            TimeLayout.setVisibility(View.GONE);
        }

        else {
            noAlarmFound.setVisibility(View.GONE);
            TimeLayout.setVisibility(View.VISIBLE);

            if (almHour > 12) {
                almHour -= 12;
                almPeriod = "PM";
            }
            else {
                almPeriod = "AM";
            }

            alarmTime.setText(String.format("%02d", almHour) + ":" + String.format("%02d", almMinute));
            alarmPeriod.setText(almPeriod);
        }
    }

    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
            alarmDays.setText("Tomorrow");
        }
        assert alarmManager != null;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, intent, 0);

        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.nav_alarm:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_timer:
                intent = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_stopwatch:
                intent = new Intent(MainActivity.this, StopWatchActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}