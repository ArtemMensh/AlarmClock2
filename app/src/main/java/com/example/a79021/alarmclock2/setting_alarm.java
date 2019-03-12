package com.example.a79021.alarmclock2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class setting_alarm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);
    }

    public void Delete(View view) {
        String id = getIntent().getStringExtra(DBHelper.KEY_ID);
        AlarmHelper alarmHelper = new AlarmHelper();

        alarmHelper.delete_alarm(this,id);

        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
