package com.zybooks.projecttwo;

import android.database.sqlite.SQLiteOpenHelper;
import static com.zybooks.projecttwo.UserDatabase.SQL_CREATE_LOGS;
import static  com.zybooks.projecttwo.UserDatabase.SQL_DELETE_LOGS;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


// Creates and helps with table functionalities related to the weight logs table.
public class WeightLogDatabaseDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Weightlogs.db";

    public WeightLogDatabaseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LOGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int olVersion, int newVersion) {
        db.execSQL(SQL_DELETE_LOGS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
