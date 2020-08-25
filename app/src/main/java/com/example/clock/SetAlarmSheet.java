package com.example.clock;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SetAlarmSheet extends BottomSheetDialogFragment {

    private LinearLayout ringtoneLayout;

    AlarmSetListener bListener;
    ImageButton done;
    TimePicker alarmTime;
    TextView alarmRingtone;

    int alarmHr = 0, alarmMin = 0;
    ArrayAdapter<String> ringtone;
    String fRingtone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.set_alarm, container, false);

        ringtoneLayout = v.findViewById(R.id.setRingtoneLayout);
        alarmTime = v.findViewById(R.id.alarmTimePicker);
        done = v.findViewById(R.id.btnAlarmSetDone);
        alarmRingtone = v.findViewById(R.id.alarm_ringtone);

        SharedPreferences preferences = requireActivity()
                .getSharedPreferences("RingtoneName", Context.MODE_PRIVATE);
        fRingtone = preferences.getString("fRingtone", "");

        alarmRingtone.setText(fRingtone);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                alarmHr = alarmTime.getHour();
                alarmMin = alarmTime.getMinute();
                bListener.onSet(alarmHr, alarmMin);
            }
        });

        ringtoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] selected = {0};

                SharedPreferences Selected = requireActivity().getSharedPreferences("RingtoneSelected", Context.MODE_PRIVATE);
                selected[0] = Selected.getInt("OptionSelectedRing", 0);

                ringtone = null;
                ringtone = new ArrayAdapter<>(requireActivity(), R.layout.dialog_item);
                ringtone.add("Mr.Bean theme");
                ringtone.add("Giorno theme");
                ringtone.add("Pirates of the Caribbean theme");
                ringtone.add("Tom & Jerry theme");
                ringtone.add("The good doctor theme");

                new AlertDialog.Builder(getContext())
                        .setTitle("Select Ringtone")
                        .setSingleChoiceItems(ringtone, selected[0], new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected[0] = which;

                                SharedPreferences Selected = requireActivity()
                                        .getSharedPreferences("RingtoneSelected", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = Selected.edit();
                                editor.putInt("OptionSelectedRing", selected[0]);
                                editor.apply();
                            }
                        })
                        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                fRingtone = ringtone.getItem(selected[0]);

                                SharedPreferences preferences = requireActivity()
                                        .getSharedPreferences("RingtoneName", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("fRingtone", fRingtone);
                                editor.apply();

                                alarmRingtone.setText(fRingtone);
                            }
                        })
                        .show();
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
