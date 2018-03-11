package com.zighter.zighterandroid.data.database;

public class StoragePointContract {
    public static final String TABLE_NAME = "Points";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_PATH_UUID = "path_uuid";
    public static final String COLUMN_EXCURSION_UUID = "excursion_uuid";

    static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER NON NULL PRIMARY KEY, "
            + COLUMN_PATH_UUID + " TEXT NON NULL, "
            + COLUMN_EXCURSION_UUID + " TEXT NON NULL, "
            + COLUMN_LONGITUDE + " REAL NON NULL, "
            + COLUMN_LATITUDE + " REAL NON NULL "
            + ");";

    static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
