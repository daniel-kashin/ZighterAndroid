package com.zighter.zighterandroid.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ZIGHTER_DATABASE";
    private static final int DATABASE_VERSION = 11;

    DatabaseOpenHelper(@NonNull Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(StorageBoughtExcursionContract.SQL_CREATE_TABLE);
        db.execSQL(StoragePointContract.SQL_CREATE_TABLE);
        db.execSQL(StoragePathContract.SQL_CREATE_TABLE);
        db.execSQL(StorageAudioContract.SQL_CREATE_TABLE);
        db.execSQL(StorageMediaContract.SQL_CREATE_TABLE);
        db.execSQL(StorageImageContract.SQL_CREATE_TABLE);
        db.execSQL(StoragePathContract.SQL_CREATE_TABLE);
        db.execSQL(StorageExcursionContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onDelete(db);
        onCreate(db);
    }

    private void onDelete(SQLiteDatabase db) {
        db.execSQL(StorageBoughtExcursionContract.SQL_DELETE_TABLE);
        db.execSQL(StoragePointContract.SQL_DELETE_TABLE);
        db.execSQL(StoragePathContract.SQL_DELETE_TABLE);
        db.execSQL(StorageAudioContract.SQL_DELETE_TABLE);
        db.execSQL(StorageMediaContract.SQL_DELETE_TABLE);
        db.execSQL(StorageImageContract.SQL_DELETE_TABLE);
        db.execSQL(StoragePathContract.SQL_DELETE_TABLE);
        db.execSQL(StorageExcursionContract.SQL_DELETE_TABLE);
    }
}
