package com.zighter.zighterandroid.data.database;

public class StorageSightContract {
    public static final String TABLE_NAME = "Sights";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_UUID = "uuid";
    public static final String COLUMN_EXCURSION_UUID = "excursion_uuid";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_TIMETABLE = "timetable";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_WEBSITE = "website";
    public static final String COLUMN_PHONE = "phone";

    static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_UUID + " TEXT NON NULL, "
            + COLUMN_EXCURSION_UUID + " TEXT NON NULL, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_LONGITUDE + " REAL NON NULL, "
            + COLUMN_LATITUDE + " REAL NON NULL, "
            + COLUMN_NAME + " TEXT NON NULL, "
            + COLUMN_TYPE + " TEXT NON NULL, "
            + COLUMN_TIMETABLE + " TEXT, "
            + COLUMN_ADDRESS + " TEXT, "
            + COLUMN_WEBSITE + " TEXT, "
            + COLUMN_PHONE + " TEXT, "
            + "UNIQUE(" + COLUMN_UUID + ", " + COLUMN_EXCURSION_UUID + ") ON CONFLICT REPLACE "
            + ");";

    static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
