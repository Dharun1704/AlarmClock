package com.example.clock;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class AlarmScreen extends AppCompatActivity {

    Random random;

    TextView num1, numOp, num2;
    EditText numAns;
    Button dismissAlarm;

    int a = 0, b = 0, c = 0, userAns;
    char[] chars;
    char op;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_wake_screen);

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

        setNumbers();

        userAns = Integer.parseInt(numAns.getText().toString());
        dismissAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userAns == c) {
                    Toast.makeText(AlarmScreen.this, "Alarm dismissed", Toast.LENGTH_SHORT).show();
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
