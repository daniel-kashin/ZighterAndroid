package com.zighter.zighterandroid.data.database;

public class StorageBoughtExcursionContract {
    public static final String TABLE_NAME = "BoughtExcursions";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_UUID = "uuid";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_OWNER = "owner";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_IS_GUIDE_AVAILABLE = "is_guide_available";
    public static final String COLUMN_IS_MEDIA_AVAILABLE = "is_media_available";
    public static final String COLUMN_IS_ROUTE_AVAILABLE = "is_route_available";
    public static final String COLUMN_IS_GUIDE_SAVED = "is_guide_saved";
    public static final String COLUMN_IS_ROUTE_SAVED = "is_route_saved";
    public static final String COLUMN_IS_MEDIA_SAVED = "is_media_saved";

    static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER NON NULL PRIMARY KEY, "
            + COLUMN_UUID + " TEXT NON NULL UNIQUE ON CONFLICT REPLACE, "
            + COLUMN_NAME + " TEXT NON NULL, "
            + COLUMN_LOCATION + " TEXT NON NULL, "
            + COLUMN_OWNER + " TEXT NON NULL, "
            + COLUMN_IMAGE_URL + " TEXT NON NULL, "
            + COLUMN_IS_GUIDE_AVAILABLE + " INTEGER NON NULL, "
            + COLUMN_IS_MEDIA_AVAILABLE + " INTEGER NON NULL, "
            + COLUMN_IS_ROUTE_AVAILABLE + " INTEGER NON NULL, "
            + COLUMN_IS_GUIDE_SAVED + " INTEGER NON NULL, "
            + COLUMN_IS_ROUTE_SAVED + " INTEGER NON NULL, "
            + COLUMN_IS_MEDIA_SAVED + " INTEGER NON NULL "
            + ");";

    static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
}
