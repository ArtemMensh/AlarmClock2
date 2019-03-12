package com.example.a79021.alarmclock2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Add_new_alarm extends AppCompatActivity {


    final boolean[] mCheckedItems = {false, false, false, false, false, false, false};
    String[] checkDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_alarm);
        checkDay = new String[]{
                getString(R.string.Sunday),
                getString(R.string.Monday),
                getString(R.string.Tuesday),
                getString(R.string.Wednesday),
                getString(R.string.Thursday),
                getString(R.string.Friday),
                getString(R.string.Saturday)
        };
        for (boolean k : mCheckedItems) {
            k = false;
        }
    }

    public void Save(View view) {
        AlarmHelper alarmHelper = new AlarmHelper();

        TimePicker timePicker = findViewById(R.id.time_picker);
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        long id = alarmHelper.add_data(this, calendar, checkDay);
        int day_week = calendar.get(Calendar.DAY_OF_WEEK);
        int next_day_repeat = alarmHelper.next_day(day_week, mCheckedItems);


        // если установленное время меньше чем время сейчас, и никакие дни не выбранны перенос будильника на следующий день
        if (calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis() && alarmHelper.all_false(mCheckedItems))
            calendar.setTimeInMillis(calendar.getTimeInMillis() + AlarmHelper.Next_Day_in_milis);
            // если установленное время больше чем время сейчас, и никакие дни не выбранны будильник на этот день
        else if (alarmHelper.all_false(mCheckedItems) && calendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis())
            calendar.setTimeInMillis(calendar.getTimeInMillis());
            // если выбрали день и он сейчас, но время уже прошло переносим будцильник на след неделю
        else if (!alarmHelper.all_false(mCheckedItems) && day_week - next_day_repeat == 0 && calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
            calendar.setTimeInMillis(calendar.getTimeInMillis() + AlarmHelper.Next_Day_in_milis * 7);
            // если выбрали день и он сейчас, но время еще не прошло устанавливаем на этот день
        else if (!alarmHelper.all_false(mCheckedItems) && day_week - next_day_repeat == 0 && calendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis())
            calendar.setTimeInMillis(calendar.getTimeInMillis());
            // устанавливаем будильники на ближайшие дни
        else if (!alarmHelper.all_false(mCheckedItems) && day_week < next_day_repeat)
            calendar.setTimeInMillis(calendar.getTimeInMillis() + AlarmHelper.Next_Day_in_milis * (next_day_repeat - day_week));
        else if (!alarmHelper.all_false(mCheckedItems) && day_week > next_day_repeat)
            calendar.setTimeInMillis(calendar.getTimeInMillis() + (Math.abs(next_day_repeat - day_week) + (day_week - next_day_repeat)) * AlarmHelper.Next_Day_in_milis);

        alarmHelper.add_new_alarm(this, calendar, id);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void Repeat(View view) {

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.choise_day))
                .setCancelable(false)

                .setMultiChoiceItems(checkDay, mCheckedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which, boolean isChecked) {
                                mCheckedItems[which] = isChecked;
                            }
                        })

                // Добавляем кнопку
                // add button
                .setPositiveButton(getString(R.string.OK),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                StringBuilder state = new StringBuilder();

                                state.append(R.string.you_choise).append(" ");
                                for (int i = 0; i < checkDay.length; i++) {
                                    if (mCheckedItems[i])
                                        state.append(checkDay[i]).append(" ");
                                }
                                Toast.makeText(getApplicationContext(),
                                        state.toString(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
        builder.show();
    }

    public void Signal(View view) {

    }
}
