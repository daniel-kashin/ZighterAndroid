package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceExcursion extends Validable<ServiceExcursion> {
    public static final int DEFAULT_MIN_MAP_ZOOM = 9;
    public static final int DEFAULT_MAX_MAP_ZOOM = 17;

    @SerializedName("id")
    private String uuid;

    @SerializedName("name")
    private String name;

    @SerializedName("route")
    private ServiceRoute route;

    @SerializedName("map_region_1")
    private ServicePoint westNorthMapBound;

    @SerializedName("map_region_2")
    private ServicePoint eastSouthMapBound;

    @SerializedName("map_zoom")
    private List<Integer> mapZoomList;

    private ServiceExcursion(@NonNull String uuid,
                             @NonNull String name,
                             //@Nullable String lastUpdateDatetime,
                             //@Nullable String createDatetime,
                             //int pubStatus,
                             //double price,
                             //@Nullable String currency,
                             @NonNull ServiceRoute route,
                             @NonNull ServicePoint westNorthMapBound,
                             @NonNull ServicePoint eastSouthMapBound,
                             @Nullable List<Integer> mapZoomList) {
        //this.lastUpdateDatetime = lastUpdateDatetime;
        //this.createDatetime = createDatetime;
        //this.pubStatus = pubStatus;
        //this.price = price;
        //this.currency = currency;

        this.uuid = uuid;
        this.name = name;
        this.route = route;
        this.westNorthMapBound = westNorthMapBound;
        this.eastSouthMapBound = eastSouthMapBound;
        this.mapZoomList = mapZoomList;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getPathSize() {
        return route.getPathSize();
    }

    public int getSightSize() {
        return route.getSightSize();
    }

    @NonNull
    public ServicePath getPathAt(int index) {
        return route.getPathAt(index);
    }

    @NonNull
    public ServiceSight getSightAt(int index) {
        return route.getSightAt(index);
    }

    @NonNull
    public ServicePoint getWestNorthMapBound() {
        return westNorthMapBound;
    }

    @NonNull
    public ServicePoint getEastSouthMapBound() {
        return eastSouthMapBound;
    }

    public int getMinMapZoom() {
        return mapZoomList != null && mapZoomList.size() >= 1 && mapZoomList.get(0) >= DEFAULT_MIN_MAP_ZOOM
                ? mapZoomList.get(0)
                : DEFAULT_MIN_MAP_ZOOM;
    }

    public int getMaxMapZoom() {
        return mapZoomList != null && mapZoomList.size() >= 2 && mapZoomList.get(1) <= DEFAULT_MAX_MAP_ZOOM
                ? mapZoomList.get(1)
                : DEFAULT_MAX_MAP_ZOOM;
    }

    @Override
    public boolean isValid() {
        return uuid != null
                && !uuid.equals("")
                && route != null
                && route.isValid();
    }

    @Override
    @Nullable
    public ServiceExcursion tryGetValid(boolean copy) {
        if (route == null
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
                                    //lastUpdateDatetime,
                                    //createDatetime,
                                    //pubStatus,
                                    //price,
                                    //currency,
                                    routeCopy,
                                    westNorthMapBound,
                                    eastSouthMapBound,
                                    mapZoomList);
    }


//@SerializedName("last_update") private String lastUpdateDatetime;
//@SerializedName("timestamp") private String createDatetime;
//@SerializedName("pub_status") private int pubStatus;
//@SerializedName("price") private double price;
//@SerializedName("currency") private String currency;

    /*
    @Nullable
    public String getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    @Nullable
    public String getCreateDatetime() {
        return createDatetime;
    }

    public int getPubStatus() {
        return pubStatus;
    }

    public double getPrice() {
        return price;
    }

    @Nullable
    public String getCurrency() {
        return currency;
    }
    */
}
