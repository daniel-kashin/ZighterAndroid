package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Excursion extends Validable<Excursion> {

    public static final int DEFAULT_MIN_MAP_ZOOM = 10;
    public static final int DEFAULT_MAX_MAP_ZOOM = 25;

    @SerializedName("id")
    private String uuid;

    @SerializedName("name")
    private String name;

    @SerializedName("last_update")
    private String lastUpdateDatetime;

    @SerializedName("timestamp")
    private String createDatetime;

    @SerializedName("pub_status")
    private int pubStatus;

    @SerializedName("price")
    private double price;

    @SerializedName("currency")
    private String currency;

    @SerializedName("user")
    private int userUuid;

    @SerializedName("route")
    private Route route;

    @SerializedName("map_region_1")
    private Point westNorthMapBound;

    @SerializedName("map_region_2")
    private Point eastSouthMapBound;

    @SerializedName("map_zoom")
    private List<Integer> mapZoomList;

    private Excursion(@NonNull String uuid,
                      @Nullable String name,
                      @Nullable String lastUpdateDatetime,
                      @Nullable String createDatetime,
                      int pubStatus,
                      double price,
                      @Nullable String currency,
                      int userUuid,
                      @NonNull Route route,
                      @Nullable Point westNorthMapBound,
                      @Nullable Point eastSouthMapBound,
                      @Nullable List<Integer> mapZoomList) {
        this.uuid = uuid;
        this.name = name;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.createDatetime = createDatetime;
        this.pubStatus = pubStatus;
        this.price = price;
        this.currency = currency;
        this.userUuid = userUuid;
        this.route = route;
        this.westNorthMapBound = westNorthMapBound;
        this.eastSouthMapBound = eastSouthMapBound;
        this.mapZoomList = mapZoomList;
    }

    public Excursion replaceRoute(@NonNull Route route) {
        return new Excursion(uuid,
                             name,
                             lastUpdateDatetime,
                             createDatetime,
                             pubStatus,
                             price,
                             currency,
                             userUuid,
                             route,
                             westNorthMapBound,
                             eastSouthMapBound,
                             mapZoomList);
    }

    @NonNull
    public String getUuid() {
        assertValid();
        return uuid;
    }

    @Nullable
    public String getName() {
        assertValid();
        return name;
    }

    @Nullable
    public String getLastUpdateDatetime() {
        assertValid();
        return lastUpdateDatetime;
    }

    @Nullable
    public String getCreateDatetime() {
        assertValid();
        return createDatetime;
    }

    public int getPubStatus() {
        assertValid();
        return pubStatus;
    }

    public double getPrice() {
        assertValid();
        return price;
    }

    @Nullable
    public String getCurrency() {
        assertValid();
        return currency;
    }

    public int getUserUuid() {
        assertValid();
        return userUuid;
    }

    public int getPathSize() {
        assertValid();
        return route.getPathSize();
    }

    public int getSightSize() {
        assertValid();
        return route.getSightSize();
    }

    @NonNull
    public Path getPathAt(int index) {
        assertValid();
        return route.getPathAt(index);
    }

    @NonNull
    public Sight getSightAt(int index) {
        assertValid();
        return route.getSightAt(index);
    }

    @Nullable
    public Point getWestNorthMapBound() {
        assertValid();
        return westNorthMapBound;
    }

    @Nullable
    public Point getEastSouthMapBound() {
        assertValid();
        return eastSouthMapBound;
    }

    public int getMinMapZoom() {
        return mapZoomList != null && mapZoomList.size() >= 1
                ? mapZoomList.get(0)
                : DEFAULT_MIN_MAP_ZOOM;
    }

    public int getMaxMapZoom() {
        return mapZoomList != null && mapZoomList.size() >= 2
                ? mapZoomList.get(1)
                : DEFAULT_MAX_MAP_ZOOM;
    }

    @Override
    public boolean isValid() {
        return userUuid != 0
                && uuid != null
                && !uuid.equals("")
                && route != null
                && route.isValid();
    }

    @Override
    public Excursion tryGetValidCopy() {
        if (route == null
                || userUuid == 0
                || uuid == null
                || uuid.equals("")) {
            return null;
        }

        Route routeCopy = route.tryGetValidCopy();
        if (routeCopy == null) {
            return null;
        }

        return new Excursion(uuid,
                             name,
                             lastUpdateDatetime,
                             createDatetime,
                             pubStatus,
                             price,
                             currency,
                             userUuid,
                             routeCopy,
                             westNorthMapBound,
                             eastSouthMapBound,
                             mapZoomList);
    }
}
