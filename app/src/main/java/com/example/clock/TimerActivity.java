package com.example.clock;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;


public class TimerActivity extends AppCompatActivity implements SetTimerSheet.BottomSheetListener, NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private NotificationManagerCompat managerCompat;

    TextView timerDisplay;
    ImageButton restartTimer, playTimer, addTimer;
    CountDownTimer countDownTimer;
    Button stopMusic;
    MediaPlayer timerEnd;

    int tmrHr, tmrMin, tmrSec;
    long tmStart = 0L, tmLeft = tmStart;
    boolean isRunning, isStop;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#252525"));
        window.setNavigationBarColor(Color.parseColor("#252525"));

        managerCompat = NotificationManagerCompat.from(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Timer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#252525"));

        drawerLayout = findViewById(R.id.timerDrawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        addTimer = findViewById(R.id.addTimer);
        playTimer = findViewById(R.id.play_tm);
        restartTimer = findViewById(R.id.restart_tm);
        timerDisplay = findViewById(R.id.timerDisplay);
        stopMusic = findViewById(R.id.stopMusicBtn);
        stopMusic.setVisibility(View.GONE);
        timerEnd = MediaPlayer.create(this, R.raw.timerendshort);
        restartTimer.setVisibility(View.INVISIBLE);
        restartTimer.setEnabled(false);
        String setTime = String.format("%02d", tmrHr) + " : " + String.format("%02d", tmrMin) + " : " + String.format("%02d", tmrSec);
        timerDisplay.setText(setTime);

        addTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetTimerSheet bottomSheet = new SetTimerSheet();
                bottomSheet.show(getSupportFragmentManager(), "TimerSheet");
            }
        });

        playTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tmStart == 0L) {
                    Toast.makeText(TimerActivity.this, "Set timer first", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!isRunning) {
                        startTimer(tmStart);
                    }
                    else {
                        pauseTimer();
                    }
                }
            }
        });

        restartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tmrSec = 0;
                tmrMin = 0;
                tmrHr = 0;
                tmStart = 0L;
                tmLeft = tmStart;
                isRunning = false;
                restartTimer.setVisibility(View.INVISIBLE);
                restartTimer.setEnabled(false);
                timerEnd.stop();
                stopMusic.setVisibility(View.INVISIBLE);
                stopMusic.setEnabled(false);
                addTimer.setVisibility(View.VISIBLE);
                addTimer.setEnabled(true);
                String setTime = String.format("%02d", tmrHr) + " : " + String.format("%02d", tmrMin) + " : " + String.format("%02d", tmrSec);
                timerDisplay.setText(setTime);

                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putLong("millisLeft", tmLeft);
                editor.putBoolean("timerRunning", isRunning);
                editor.putLong("startTime", tmStart);

                editor.apply();
            }
        });

        stopMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerEnd.stop();
                stopMusic.setVisibility(View.GONE);
                addTimer.setVisibility(View.VISIBLE);
                addTimer.setEnabled(true);
                restartTimer.setVisibility(View.INVISIBLE);
                restartTimer.setEnabled(false);
            }
        });
    }

    private void startTimer(long start) {
        countDownTimer = new CountDownTimer(start, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tmLeft = millisUntilFinished;
                int sec = (int) (tmLeft / 1000);
                int min = sec / 60;
                sec %= 60;
                int hr = min / 60;
                min %= 60;
                sec %= 60;
                String timeLeft = String.format("%02d : %02d : %02d", hr, min, sec);
                timerDisplay.setText(timeLeft);
            }

            @Override
            public void onFinish() {
                isRunning = false;
                isStop = true;
                stopMusic.setVisibility(View.VISIBLE);
                stopMusic.setEnabled(true);
                addTimer.setVisibility(View.INVISIBLE);
                addTimer.setEnabled(false);
                playTimer.setImageResource(R.drawable.ic_play);
                tmStart = 0L;
                tmLeft = tmStart;

                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putLong("millisLeft", tmLeft);
                editor.putBoolean("timerRunning", isRunning);
                editor.putLong("startTime", tmStart);

                editor.apply();

                Notification notification = new NotificationCompat.Builder(TimerActivity.this, "Timer")
                        .setSmallIcon(R.drawable.ic_timer)
                        .setContentTitle("Timer ended!")
                        .setContentText("Your timer has come to an end.")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .build();

                managerCompat.notify(1, notification);

                timerEnd.start();
                restartTimer.setVisibility(View.VISIBLE);
                restartTimer.setEnabled(true);
            }
        }.start();
        isRunning = true;
        isStop = false;
        stopMusic.setVisibility(View.INVISIBLE);
        stopMusic.setEnabled(false);
        playTimer.setImageResource(R.drawable.ic_pause);
        restartTimer.setVisibility(View.INVISIBLE);
        restartTimer.setEnabled(false);
        addTimer.setVisibility(View.INVISIBLE);
        addTimer.setEnabled(false);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isRunning = false;
        isStop = false;
        stopMusic.setVisibility(View.INVISIBLE);
        stopMusic.setEnabled(false);
        playTimer.setImageResource(R.drawable.ic_play);
        restartTimer.setEnabled(true);
        restartTimer.setVisibility(View.VISIBLE);
    }

    private long convertToMillis(int a, int b, int c) {
        return  ((a * 3600) + (b * 60) + (c)) * 1000;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onClicked(int hr, int min, int sec) {
        tmrSec = sec;
        tmrMin = min;
        tmrHr = hr;

        String setTime = String.format("%02d", tmrHr) + " : " + String.format("%02d", tmrMin) + " : " + String.format("%02d", tmrSec);
        timerDisplay.setText(setTime);
        tmStart = convertToMillis(tmrHr, tmrMin, tmrSec);
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
                intent = new Intent(TimerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_timer:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_stopwatch:
                intent = new Intent(TimerActivity.this, StopWatchActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", tmLeft);
        editor.putBoolean("timerRunning", isRunning);
        editor.putLong("startTime", tmStart);

        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        tmLeft = prefs.getLong("millisLeft", tmStart);
        isRunning = prefs.getBoolean("timerRunning", false);
        int sec = (int) (tmLeft / 1000);
        int min = sec / 60;
        sec %= 60;
        int hr = min / 60;
        min %= 60;
        sec %= 60;
        String timeLeft = String.format("%02d : %02d : %02d", hr, min, sec);
        timerDisplay.setText(timeLeft);
        if (tmLeft <= 0) {
            tmLeft = 0;
            isRunning = false;
            sec = (int) (tmLeft / 1000);
            min = sec / 60;
            hr = min / 60;
            timeLeft = String.format("%02d : %02d : %02d", hr, min, sec);
            timerDisplay.setText(timeLeft);
        }
        else {
            isRunning = true;
            tmStart = tmLeft;
            tmLeft = tmStart;
            startTimer(tmStart);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        tmLeft = prefs.getLong("millisLeft", tmStart);
        isRunning = prefs.getBoolean("timerRunning", false);
        int sec = (int) (tmLeft / 1000);
        int min = sec / 60;
        sec %= 60;
        int hr = min / 60;
        min %= 60;
        sec %= 60;
        String timeLeft = String.format("%02d : %02d : %02d", hr, min, sec);
        timerDisplay.setText(timeLeft);
        if (tmLeft <= 0) {
            tmLeft = 0;
            isRunning = false;
            sec = (int) (tmLeft / 1000);
            min = sec / 60;
            hr = min / 60;
            timeLeft = String.format("%02d : %02d : %02d", hr, min, sec);
            timerDisplay.setText(timeLeft);
        }
        else {
            isRunning = true;
            tmStart = tmLeft;
            tmLeft = tmStart;
            startTimer(tmStart);
        }
    }
}
