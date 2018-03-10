package com.zighter.zighterandroid.data.database;

public class StoragePathContract {
    public static final String TABLE_NAME = "Paths";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_UUID = "uuid";
    public static final String COLUMN_EXCURSION_UUID = "excursion_uuid";

    static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER NON NULL PRIMARY KEY, "
            + COLUMN_UUID + " TEXT NON NULL UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_EXCURSION_UUID + " TEXT NON NULL "
            + ");";

    static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
