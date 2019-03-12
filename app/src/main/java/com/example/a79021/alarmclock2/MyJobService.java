package com.example.a79021.alarmclock2;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.Objects;

public class MyJobService extends JobService {

    @Override
    public boolean onStartJob(@NonNull JobParameters job) {

        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));
        firebaseJobDispatcher.cancel(job.getTag());

        Intent intent = new Intent(this, alarm.class);
        String s = Objects.requireNonNull(job.getExtras()).getString(DBHelper.KEY_ID);
        intent.putExtra(DBHelper.KEY_ID, s);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);

        return false;
    }

    @Override
    public boolean onStopJob(@NonNull JobParameters job) {
        return false;
    }
}

