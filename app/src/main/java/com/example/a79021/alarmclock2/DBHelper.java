package com.example.a79021.alarmclock2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MAIN";
    public static final String TABLE_NAME = "mytable";
    public static final String KEY_ID = "_id";
    public static final String TIME = "time";

    public static final String SUNDAY = "sunday";
    public static final String MONDAY = "monday";
    public static final String TUESDAY = "tuesday";
    public static final String WEDNESDAY = "wednesday";
    public static final String THURSDAY = "thursday";
    public static final String FRIDAY = "friday";
    public static final String SATURDAY = "saturday";

    public static final String SIGNAL = "signal";
    public static final String HELLO = "hello";
    public static final String RETRY = "retry";
    public static final String COAST = "coast";
    public static final String TIME_REPEAT = "time_repeat";
    public static final String ACCOUNT_BANK = "account_bank";

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // первое создание базы данных
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ("
                + KEY_ID+" integer primary key autoincrement,"
                + TIME + " text,"

                + SUNDAY + " text,"
                + MONDAY + " text,"
                + TUESDAY + " text,"
                + WEDNESDAY + " text,"
                + THURSDAY + " text,"
                + FRIDAY + " text,"
                + SATURDAY + " text,"

                + SIGNAL+" text,"
                + HELLO+" text,"
                + RETRY+" integer,"
                + COAST+" real,"
                + TIME_REPEAT+" integer,"
                + ACCOUNT_BANK+" text"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists mytable");
        onCreate(db);
    }
}
