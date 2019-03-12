package com.example.a79021.alarmclock2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Objects;

public class alarm extends AppCompatActivity {

    private MediaPlayer sound;
    String id;
    String[] choise_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        id = getIntent().getStringExtra(DBHelper.KEY_ID);

        TextView textView = findViewById(R.id.string);

        // For on screen when it blocked
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        // Read datebase
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            final int index_id = cursor.getColumnIndex(DBHelper.KEY_ID);
            final int index_saturday = cursor.getColumnIndex(DBHelper.SATURDAY);
            int int_time = cursor.getColumnIndex(DBHelper.TIME);

            do {
                if (cursor.getInt(index_id) == Integer.valueOf(id)) {
                    String s = cursor.getString(int_time);
                    textView.setText(s);

                    // заполняем массив выбранных дней
                    choise_day = new String[]{
                            cursor.getString(index_saturday),
                            cursor.getString(index_saturday + 1),
                            cursor.getString(index_saturday + 2),
                            cursor.getString(index_saturday + 3),
                            cursor.getString(index_saturday + 4),
                            cursor.getString(index_saturday + 5),
                            cursor.getString(index_saturday + 6),
                    };


                }
            } while (cursor.moveToNext());
            cursor.close();
        }


        db.delete(DBHelper.TABLE_NAME, DBHelper.KEY_ID + "=" + id, null);

        sound = MediaPlayer.create(this, R.raw.wake_up);
        sound.start();
    }

    public void Stop(View view) {
        sound.stop();
        AlarmHelper alarmHelper = new AlarmHelper();
        boolean[] b = alarmHelper.from_string_to_bool(choise_day);

        Calendar calendar = Calendar.getInstance();
        int day_week = calendar.get(Calendar.DAY_OF_WEEK);
        int next_day_repeat = alarmHelper.next_day(day_week, b);

        if (alarmHelper.all_false(b))
            alarmHelper.delete_alarm(this, id);
        else if (day_week - next_day_repeat == 0 && calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())
            calendar.setTimeInMillis(calendar.getTimeInMillis() + AlarmHelper.Next_Day_in_milis * 7);
            // если выбрали день и он сейчас, но время еще не прошло устанавливаем на этот день
        else if (day_week - next_day_repeat == 0 && calendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis())
            calendar.setTimeInMillis(calendar.getTimeInMillis());
            // устанавливаем будильники на ближайшие дни
        else if (day_week < next_day_repeat)
            calendar.setTimeInMillis(calendar.getTimeInMillis() + AlarmHelper.Next_Day_in_milis * (next_day_repeat - day_week));
        else if (day_week > next_day_repeat)
            calendar.setTimeInMillis(calendar.getTimeInMillis() + (Math.abs(next_day_repeat - day_week) + (day_week - next_day_repeat)) * AlarmHelper.Next_Day_in_milis);

        if (!alarmHelper.all_false(b))
            alarmHelper.add_new_alarm(this, calendar, Long.valueOf(id));

        finish();
    }
}
