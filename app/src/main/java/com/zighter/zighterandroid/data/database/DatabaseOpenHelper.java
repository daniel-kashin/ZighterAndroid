package com.zighter.zighterandroid.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ZIGHTER_DATABASE";
    private static final int DATABASE_VERSION = 10;

    DatabaseOpenHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(StorageBoughtExcursionContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(StorageBoughtExcursionContract.SQL_DELETE_TABLE);
        db.execSQL(StorageBoughtExcursionContract.SQL_CREATE_TABLE);
    }
}
