package com.zighter.zighterandroid.data.database;

public class StorageBoughtExcursionContract {
    public static final String TABLE_NAME = "BOUGHT_EXCURSIONS";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_UUID = "uuid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_OWNER = "owner";
    public static final String COLUMN_IMAGE_URL = "imageUrl";
    public static final String COLUMN_IS_GUIDE_AVAILABLE = "isGuideAvailable";
    public static final String COLUMN_IS_MEDIA_AVAILABLE = "isMediaAvailable";
    public static final String COLUMN_IS_ROUTE_AVAILABLE = "isRouteAvailable";
    public static final String COLUMN_IS_FULLY_SAVED = "isFullySaved";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER NON NULL PRIMARY KEY, "
            + COLUMN_UUID + " TEXT NON NULL UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_NAME + " TEXT NON NULL, "
            + COLUMN_LOCATION + " TEXT NON NULL, "
            + COLUMN_OWNER + " TEXT NON NULL, "
            + COLUMN_IMAGE_URL + " TEXT, "
            + COLUMN_IS_GUIDE_AVAILABLE + " INTEGER NON NULL, "
            + COLUMN_IS_MEDIA_AVAILABLE + " INTEGER NON NULL, "
            + COLUMN_IS_ROUTE_AVAILABLE + " INTEGER NON NULL, "
            + COLUMN_IS_FULLY_SAVED + " INTEGER NON NULL "
            + ");";

    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
