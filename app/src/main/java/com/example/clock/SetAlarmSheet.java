package com.example.clock;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class SetAlarmSheet extends BottomSheetDialogFragment {

    AlarmSetListener bListener;
    ImageButton done;
    Button[] days;

    TimePicker alarmTime;
    int alarmHr = 0, alarmMin = 0;
    boolean[] isDays;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.set_alarm, container, false);

        alarmTime = v.findViewById(R.id.alarmTimePicker);
        done = v.findViewById(R.id.btnAlarmSetDone);
        days = new Button[7];
        days[0] = v.findViewById(R.id.btnSunday);
        days[1] = v.findViewById(R.id.btnMonday);
        days[2] = v.findViewById(R.id.btnTuesday);
        days[3] = v.findViewById(R.id.btnWednesday);
        days[4] = v.findViewById(R.id.btnThursday);
        days[5] = v.findViewById(R.id.btnFriday);
        days[6] = v.findViewById(R.id.btnSaturday);

        isDays = new boolean[7];
        for (int i = 0; i < 7; i++) {
            isDays[i] = false;
        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                alarmHr = alarmTime.getHour();
                alarmMin = alarmTime.getMinute();
                bListener.onSet(alarmHr, alarmMin);
            }
        });

        days[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDays[0]) {
                    days[0].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_alarmdone));
                    days[0].setTextColor(Color.parseColor("#252525"));
                    isDays[0] = true;
                }
                else {
                    days[0].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_repeat));
                    days[0].setTextColor(Color.WHITE);
                    isDays[0] = false;
                }
            }
        });

        days[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDays[1]) {
                    days[1].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_alarmdone));
                    days[1].setTextColor(Color.parseColor("#252525"));
                    isDays[1] = true;
                }
                else {
                    days[1].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_repeat));
                    days[1].setTextColor(Color.WHITE);
                    isDays[1] = false;
                }
            }
        });

        days[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDays[2]) {
                    days[2].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_alarmdone));
                    days[2].setTextColor(Color.parseColor("#252525"));
                    isDays[2] = true;
                }
                else {
                    days[2].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_repeat));
                    days[2].setTextColor(Color.WHITE);
                    isDays[2] = false;
                }
            }
        });

        days[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDays[3]) {
                    days[3].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_alarmdone));
                    isDays[3] = true;
                    days[3].setTextColor(Color.parseColor("#252525"));
                }
                else {
                    days[3].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_repeat));
                    days[3].setTextColor(Color.WHITE);
                    isDays[3] = false;
                }
            }
        });

        days[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDays[4]) {
                    days[4].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_alarmdone));
                    days[4].setTextColor(Color.parseColor("#252525"));
                    isDays[4] = true;
                }
                else {
                    days[4].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_repeat));
                    days[4].setTextColor(Color.WHITE);
                    isDays[4] = false;
                }
            }
        });

        days[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDays[5]) {
                    days[5].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_alarmdone));
                    days[5].setTextColor(Color.parseColor("#252525"));
                    isDays[5] = true;
                }
                else {
                    days[5].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_repeat));
                    days[5].setTextColor(Color.WHITE);
                    isDays[5] = false;
                }
            }
        });

        days[6].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDays[6]) {
                    days[6].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_alarmdone));
                    days[6].setTextColor(Color.parseColor("#252525"));
                    isDays[6] = true;
                }
                else {
                    days[6].setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_repeat));
                    days[6].setTextColor(Color.WHITE);
                    isDays[6] = false;
                }
            }
        });

        return v;
    }

    public interface AlarmSetListener {
        void onSet(int hour, int minute);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bListener = (AlarmSetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement BottomSheetListener");
        }
    }
}
