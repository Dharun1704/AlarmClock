package com.example.clock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SetTimerSheet extends BottomSheetDialogFragment {

    private BottomSheetListener bListener;

    Button timerStart;
    NumberPicker hrPicker, minPicker, secPicker;

    int hr, min, sec;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.set_timer, container, false);

        timerStart = v.findViewById(R.id.timerStart);

        hrPicker = v.findViewById(R.id.hrPicker);
        hrPicker.setMinValue(0);
        hrPicker.setMaxValue(23);
        hrPicker.setFormatter(new NumberPicker.Formatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });
        minPicker = v.findViewById(R.id.minPicker);
        minPicker.setMaxValue(59);
        minPicker.setMinValue(0);
        minPicker.setFormatter(new NumberPicker.Formatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });
        secPicker = v.findViewById(R.id.secPicker);
        secPicker.setMinValue(0);
        secPicker.setMaxValue(59);
        secPicker.setFormatter(new NumberPicker.Formatter() {
            @SuppressLint("DefaultLocale")
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        timerStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hr = hrPicker.getValue();
                min = minPicker.getValue();
                sec = secPicker.getValue();
                bListener.onClicked(hr, min, sec);
                dismiss();
            }
        });

        return v;
    }

    public interface BottomSheetListener {
        void onClicked(int hr, int min, int sec);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bListener = (BottomSheetListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement BottomSheetListener");
        }
    }
}