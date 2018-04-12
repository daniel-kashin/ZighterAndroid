package com.zighter.zighterandroid.data.database;

public class StorageGuideContract {
    public static final String TABLE_NAME = "Guides";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_UUID = "uuid";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_EMAIL = "email";

    static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER NON NULL PRIMARY KEY, "
            + COLUMN_UUID + " TEXT NON NULL UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_USERNAME + " TEXT NON NULL, "
            + COLUMN_FIRST_NAME + " TEXT NON NULL, "
            + COLUMN_LAST_NAME + " TEXT, "
            + COLUMN_PHONE + " TEXT NON NULL, "
            + COLUMN_EMAIL + " TEXT NON NULL"
            + ");";

    static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
