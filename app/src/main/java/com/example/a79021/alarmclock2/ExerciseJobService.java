package com.example.a79021.alarmclock2;

import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;

public class ExerciseJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        Intent intent = new Intent(this, alarm.class);


        intent.putExtra(DBHelper.KEY_ID, String.valueOf(params.getJobId()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
