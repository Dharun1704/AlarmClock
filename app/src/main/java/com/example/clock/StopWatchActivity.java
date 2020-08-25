package com.example.clock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Objects;

public class StopWatchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Handler handler;
    ScrollView lapScroll;
    LinearLayout lapContainer;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;

    TextView time;
    ImageButton play, restart, lap;
    private boolean isResume;

    long tMillis = 0L, tStart = 0L, tBuff = 0L, tUpdate = 0L;
    long curr = 0, last = 0;
    int lapCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Stop watch");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#252525"));

        drawerLayout = findViewById(R.id.stopwatchDrawerLayout);
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#252525"));
        window.setNavigationBarColor(Color.parseColor("#252525"));

        lapContainer = findViewById(R.id.lapContainer);
        time = findViewById(R.id.time_sw);
        lapScroll = findViewById(R.id.lapScroll);
        handler = new Handler();
        play = findViewById(R.id.play_sw);
        restart = findViewById(R.id.restart_sw);
        lap = findViewById(R.id.lap_sw);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isResume) {
                    tStart = SystemClock.uptimeMillis();
                    handler.postDelayed(updateTime, 0);
                    restart.setVisibility(View.INVISIBLE);
                    restart.setEnabled(false);
                    lap.setEnabled(true);
                    isResume = true;
                    lap.setVisibility(View.VISIBLE);
                    lap.setEnabled(true);
                    play.setImageResource(R.drawable.ic_pause);
                }

                else {
                    tBuff += tMillis;
                    handler.removeCallbacks(updateTime);
                    isResume = false;
                    restart.setVisibility(View.VISIBLE);
                    restart.setEnabled(true);
                    play.setImageResource(R.drawable.ic_play);
                    lap.setEnabled(false);
                    lap.setVisibility(View.INVISIBLE);
                }
            }
        });

        lap.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "InflateParams"})
            @Override
            public void onClick(View v) {
                LayoutInflater inflater =(LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert inflater != null;
                View addLap = inflater.inflate(R.layout.lap_item, null);

                TextView lapNo, lapTime, lapDiff;
                lapNo = addLap.findViewById(R.id.lap_count);
                lapTime = addLap.findViewById(R.id.lapTime);
                lapDiff = addLap.findViewById(R.id.lap_diff);

                lapCount++;
                lapNo.setText(String.valueOf(lapCount));
                lapTime.setText(time.getText().toString());
                if (lapCount == 1) {
                    curr = tUpdate;
                    last = 0;
                    lapDiff.setText(time.getText().toString());
                }
                else {
                    last = curr;
                    curr = tUpdate;
                    long diff = curr - last;
                    int milliSec = (int) (diff % 1000) / 10;
                    int sec = (int) (diff / 1000);
                    int min = sec / 60;
                    sec %= 60;
                    lapDiff.setText(String.format("%02d",min) + ":" + String.format("%02d", sec) + ":" +
                            String.format("%02d", milliSec));
                }
                lapContainer.addView(addLap);
                lapScroll.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tMillis = 0L;
                tStart = 0L;
                tBuff = 0L;
                tUpdate = 0L;
                time.setText("00:00:00");
                lapCount = 0;
                curr = 0L;
                last = 0L;
                lapContainer.removeAllViews();
                restart.setVisibility(View.INVISIBLE);
                restart.setEnabled(false);
            }
        });
    }

    private Runnable updateTime = new Runnable() {
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void run() {
            tMillis = SystemClock.uptimeMillis() - tStart;
            tUpdate = tBuff + tMillis;
            int milliSec = (int) (tUpdate % 1000) / 10;
            int sec = (int) (tUpdate / 1000);
            int min = sec / 60;
            sec %= 60;
            time.setText(String.format("%02d",min) + ":" + String.format("%02d", sec) + ":" + String.format("%02d", milliSec));
            handler.postDelayed(updateTime, 0);
        }
    };

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
                intent = new Intent(StopWatchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_timer:
                intent = new Intent(StopWatchActivity.this, TimerActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_stopwatch:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
