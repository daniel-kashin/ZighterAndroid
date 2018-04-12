package com.zighter.zighterandroid.data.database;

public class StorageExcursionContract {
    public static final String TABLE_NAME = "Routes";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_UUID = "uuid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_WEST_BOUND = "west_bound";
    public static final String COLUMN_NORTH_BOUND = "north_bound";
    public static final String COLUMN_EAST_BOUND = "east_bound";
    public static final String COLUMN_SOUTH_BOUND = "south_bound";
    public static final String COLUMN_MIN_ZOOM = "min_zoom";
    public static final String COLUMN_MAX_ZOOM = "max_zoom";

    static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER NON NULL PRIMARY KEY, "
            + COLUMN_UUID + " TEXT NON NULL UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_NAME + " TEXT NON NULL, "
            + COLUMN_WEST_BOUND + " REAL NON NULL, "
            + COLUMN_EAST_BOUND + " REAL NON NULL, "
            + COLUMN_NORTH_BOUND + " REAL NON NULL, "
            + COLUMN_SOUTH_BOUND + " REAL NON NULL, "
            + COLUMN_MIN_ZOOM + " INTEGER NON NULL, "
            + COLUMN_MAX_ZOOM + " INTEGER NON NULL "
            + ");";

    static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
