package com.zighter.zighterandroid.data.database;

public class StorageMediaContract {
    public static final String TABLE_NAME = "Medias";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_SIGHT_UUID = "sight_uuid";
    public static final String COLUMN_EXCURSION_UUID = "excursion_uuid";
    public static final String COLUMN_TYPE = "type";

    static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER NON NULL PRIMARY KEY, "
            + COLUMN_SIGHT_UUID + " TEXT NON NULL, "
            + COLUMN_EXCURSION_UUID + " TEXT NON NULL, "
            + COLUMN_TYPE + " TEXT NON NULL, "
            + COLUMN_URL + " TEXT NON NULL, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_TITLE + " TEXT "
            + ");";

    static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
