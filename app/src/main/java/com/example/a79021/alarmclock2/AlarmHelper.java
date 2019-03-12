package com.example.a79021.alarmclock2;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

class AlarmHelper {

    static int Next_Day_in_milis = 86400000;

    void add_new_alarm(Context context, Calendar calendar, long id_long) {

        long time = calendar.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        // from millisecond in second
        time = time / 1000;

        int change_time = (int) time;

        int id = (int) id_long;


        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context.getApplicationContext()));

        Job myJob = firebaseJobDispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(MyJobService.class)
                // uniquely identifies the job
                .setTag(String.valueOf(id))
                // one-off job
                .setRecurring(false)
                // don't persist past a device reboot
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                // start between change_time and change_time+5 seconds from now
                .setTrigger(Trigger.executionWindow(change_time, change_time+5))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .build();

        firebaseJobDispatcher.mustSchedule(myJob);

        ComponentName serviceComponent = new ComponentName(context, ExerciseJobService.class);

        JobInfo.Builder exerciseJobBuilder = new JobInfo.Builder(id, serviceComponent);

        // время отложенного старта
        exerciseJobBuilder.setMinimumLatency(TimeUnit.SECONDS.toMillis(change_time));

        // время при котором выполнится в любом случае
        exerciseJobBuilder.setOverrideDeadline(TimeUnit.SECONDS.toMillis(change_time + 5));

        // сохраняется ли при перезагрузке
        exerciseJobBuilder.setPersisted(true);

        // Определяет состояние, когда пользователь не взаимодействует с устройством
        exerciseJobBuilder.setRequiresDeviceIdle(false);

        // без разницы, заряжается устройство или нет
        exerciseJobBuilder.setRequiresCharging(false);

        JobScheduler jobScheduler =
                (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        jobScheduler.schedule(exerciseJobBuilder.build());
    }

    long add_data(Context context, Calendar calendar, String[] checkDay) {
        // создаем объект для данных
        ContentValues cv = new ContentValues();

        DBHelper dbHelper = new DBHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String minute = String.valueOf(calendar.get(Calendar.MINUTE));

        if (Integer.valueOf(minute) < 10)
            minute = "0" + minute;
        String time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + minute;

        cv.put(DBHelper.TIME, time);

        //
        cv.put(DBHelper.SUNDAY, checkDay[0]);
        cv.put(DBHelper.MONDAY, checkDay[1]);
        cv.put(DBHelper.TUESDAY, checkDay[2]);
        cv.put(DBHelper.WEDNESDAY, checkDay[3]);
        cv.put(DBHelper.THURSDAY, checkDay[4]);
        cv.put(DBHelper.FRIDAY, checkDay[5]);
        cv.put(DBHelper.SATURDAY, checkDay[6]);

        Toast toast = Toast.makeText(context, "Будильник установлен на " + time, Toast.LENGTH_SHORT);
        toast.show();

        return db.insert(DBHelper.TABLE_NAME, null, cv);
    }

    void delete_alarm(Context context, String id) {
        // delete info about alarm from Job Service

        JobScheduler jobScheduler =
                (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(Integer.valueOf(id));

        //  delete info about alarm from data base
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String[] s = new String[]{id};

        database.delete(DBHelper.TABLE_NAME, DBHelper.KEY_ID + "= ?", s);
        database.close();
    }

    int next_day(int day_week, boolean[] mCheckedItems) {

        for (int i = day_week; i < 7; i++) {
            if (mCheckedItems[i]) return i;
        }
        for (int i = 0; i < day_week; i++) {
            if (mCheckedItems[i]) return i;
        }
        return 0;

    }

    boolean all_false(boolean[] mCheckedItems) {
        for (boolean s : mCheckedItems) {
            if (s)
                return false;
        }
        return true;
    }

    boolean[] from_string_to_bool(String[] s) {
        boolean[] b = new boolean[7];

        for (int i = 0; i < 7; i++) {
            b[i] = Boolean.valueOf(s[i]);
        }

        return b;
    }


}
