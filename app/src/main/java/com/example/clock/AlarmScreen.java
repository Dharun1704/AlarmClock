package com.example.clock;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class AlarmScreen extends AppCompatActivity {

    Random random;
    MediaPlayer mediaPlayer;

    TextView num1, numOp, num2;
    EditText numAns;
    Button dismissAlarm;

    int a = 0, b = 0, c = 0, userAns;
    char[] chars;
    char op;
    String ringtone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_wake_screen);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#252525"));
        window.setNavigationBarColor(Color.parseColor("#252525"));

        random = new Random();

        num1 = findViewById(R.id.number_1);
        numOp = findViewById(R.id.number_operator);
        num2 = findViewById(R.id.number_2);
        numAns = findViewById(R.id.number_answer);
        dismissAlarm = findViewById(R.id.dismissAlarm);

        chars = new char[3];
        chars[0] = '+';
        chars[1] = '-';
        chars[2] = '*';
        SharedPreferences sRingtone = getSharedPreferences("AlarmRingtone", MODE_PRIVATE);
        ringtone = sRingtone.getString("alarmRingtone", "");

        mediaPlayer = new MediaPlayer();

        assert ringtone != null;
        switch (ringtone) {
            case "Mr.Bean theme":
                mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.bean_song);
                mediaPlayer.start();
                break;
            case "Giorno theme":
                mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.giorno_theme);
                mediaPlayer.start();
                break;
            case "Pirates of the Caribbean theme":
                mediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.pirates_ot_caribbean);
                mediaPlayer.start();
                break;
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });

        setNumbers();

        dismissAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numAns.getText() == null)
                    Toast.makeText(AlarmScreen.this, "Enter valid answer", Toast.LENGTH_SHORT).show();
                else
                    userAns = Integer.parseInt(numAns.getText().toString());

                if (userAns == c) {
                    Toast.makeText(AlarmScreen.this, "Alarm dismissed", Toast.LENGTH_SHORT).show();
                    mediaPlayer.stop();
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent i = new Intent(AlarmScreen.this, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmScreen.this, 1, i, 0);

                    assert alarmManager != null;
                    alarmManager.cancel(pendingIntent);

                    SharedPreferences sHour = getSharedPreferences("AlarmHour", MODE_PRIVATE);
                    SharedPreferences.Editor editorH = sHour.edit();
                    editorH.putInt("almHour", 0);
                    editorH.apply();

                    SharedPreferences sMinute = getSharedPreferences("AlarmMinute", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sMinute.edit();
                    editor.putInt("almMinute", 0);
                    editor.apply();

                    Intent intent = new Intent(AlarmScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    Toast.makeText(AlarmScreen.this, "Oops! Wrong answer!", Toast.LENGTH_SHORT).show();
                    setNumbers();

                }
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void setNumbers() {
        op = chars[random.nextInt(3)];
        numOp.setText(Character.toString(op));
        switch (op) {
            case '+':
                a = random.nextInt(100);
                b = random.nextInt(100);
                num1.setText(String.format("%02d", a));
                num2.setText(String.format("%02d", b));
                c = a + b;
                break;
            case '-':
                a = random.nextInt(100);
                do {
                    b = random.nextInt(100);
                } while (b < a);
                num1.setText(String.format("%02d", a));
                num2.setText(String.format("%02d", b));
                c = a - b;
                break;
            case '*':
                a = random.nextInt(10);
                b = random.nextInt(10);
                num1.setText(String.format("%02d", a));
                num2.setText(String.format("%02d", b));
                c = a * b;
                break;
        }
    }
}
