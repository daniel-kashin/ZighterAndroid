package com.zighter.zighterandroid.data.entities.excursion;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ServiceExcursion extends Validable<ServiceExcursion> {
    private static final int DEFAULT_MIN_MAP_ZOOM = 8;
    private static final int DEFAULT_MAX_MAP_ZOOM = 18;

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
    private ServiceRoute route;

    @SerializedName("map_region_1")
    private ServicePoint westNorthMapBound;

    @SerializedName("map_region_2")
    private ServicePoint eastSouthMapBound;

    @SerializedName("map_zoom")
    private List<Integer> mapZoomList;

    private ServiceExcursion(@NonNull String uuid,
                             @Nullable String name,
                             @Nullable String lastUpdateDatetime,
                             @Nullable String createDatetime,
                             int pubStatus,
                             double price,
                             @Nullable String currency,
                             int userUuid,
                             @NonNull ServiceRoute route,
                             @Nullable ServicePoint westNorthMapBound,
                             @Nullable ServicePoint eastSouthMapBound,
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

    @NonNull
    String getUuid() {
        return uuid;
    }

    @Nullable
    String getName() {
        return name;
    }

    @Nullable
    String getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    @Nullable
    String getCreateDatetime() {
        return createDatetime;
    }

    int getPubStatus() {
        return pubStatus;
    }

    double getPrice() {
        return price;
    }

    @Nullable
    String getCurrency() {
        return currency;
    }

    int getUserUuid() {
        return userUuid;
    }

    int getPathSize() {
        return route.getPathSize();
    }

    int getSightSize() {
        return route.getSightSize();
    }

    @NonNull
    ServicePath getPathAt(int index) {
        return route.getPathAt(index);
    }

    @NonNull
    ServiceSight getSightAt(int index) {
        return route.getSightAt(index);
    }

    @Nullable
    ServicePoint getWestNorthMapBound() {
        return westNorthMapBound;
    }

    @Nullable
    ServicePoint getEastSouthMapBound() {
        return eastSouthMapBound;
    }

    int getMinMapZoom() {
        return //mapZoomList != null && mapZoomList.size() >= 1 && mapZoomList.get(0) >= DEFAULT_MIN_MAP_ZOOM
                // ? mapZoomList.get(0)
                // :
                DEFAULT_MIN_MAP_ZOOM;
    }

    int getMaxMapZoom() {
        return //mapZoomList != null && mapZoomList.size() >= 2 && mapZoomList.get(1) <= DEFAULT_MAX_MAP_ZOOM
                // ? mapZoomList.get(1)
                // :
                DEFAULT_MAX_MAP_ZOOM;
    }

    @Override
    boolean isValid() {
        return userUuid != 0
                && uuid != null
                && !uuid.equals("")
                && route != null
                && route.isValid();
    }

    @Override
    @Nullable
    ServiceExcursion tryGetValid(boolean copy) {
        if (route == null
                || userUuid == 0
                || uuid == null
                || uuid.equals("")) {
            return null;
        }

        ServiceRoute routeCopy = route.tryGetValid(true);
        if (routeCopy == null) {
            return null;
        }

        if (!copy) {
            return this;
        }

        return new ServiceExcursion(uuid,
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