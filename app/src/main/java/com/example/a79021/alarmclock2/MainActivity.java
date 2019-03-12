package com.example.a79021.alarmclock2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    LinearLayout main_linear;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main_linear = findViewById(R.id.main_linear);

        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        cursor = database.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            final int int_id = cursor.getColumnIndex(DBHelper.KEY_ID);
            int int_time = cursor.getColumnIndex(DBHelper.TIME);

            do {
                final int id = cursor.getInt(int_id);
                final TextView textView = new TextView(this);
                textView.setText(cursor.getString(int_time));
                textView.setTextSize(22);
                textView.setGravity(Gravity.CENTER);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, setting_alarm.class);
                        intent.putExtra(DBHelper.KEY_ID, String.valueOf(id));
                        startActivity(intent);
                    }
                });
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                main_linear.addView(textView);

                View view = new View(this);
                view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
                view.setBackgroundColor(Color.BLACK);
                main_linear.addView(view);
            } while (cursor.moveToNext());
        }
        else {
            main_linear = findViewById(R.id.main_linear);
            main_linear.setBackgroundResource(R.drawable.dasha);
        }
    }

    @Override
    public void finish() {
        super.finish();
        cursor.close();
    }

    public void Add(View view) {
        Intent intent = new Intent(this, Add_new_alarm.class);
        startActivity(intent);
    }
}
