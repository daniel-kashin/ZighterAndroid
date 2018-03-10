package com.zighter.zighterandroid.data.entities.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zighter.zighterandroid.data.entities.service.ServicePath;
import com.zighter.zighterandroid.data.entities.service.ServicePoint;

import java.io.Serializable;
import java.util.List;

public class Excursion implements Serializable {
    @NonNull
    private final String uuid;
    @NonNull
    private final String name;
    //@Nullable
    //private final String lastUpdateDatetime;
    //@Nullable
    //private final String createDatetime;
    //private final int pubStatus;
    //private final double price;
    //@Nullable
    //private final String currency;

    @NonNull
    private List<Sight> sights;
    @NonNull
    private List<ServicePath> paths;
    @NonNull
    private ServicePoint westNorthMapBound;
    @NonNull
    private ServicePoint eastSouthMapBound;
    private int maxMapZoom;
    private int minMapZoom;

    public Excursion(@NonNull String uuid,
                     @NonNull String name,
                     //@Nullable String lastUpdateDatetime,
                     //@Nullable String createDatetime,
                     //int pubStatus,
                     //double price,
                     //@Nullable String currency,
                     @NonNull List<Sight> sights,
                     @NonNull List<ServicePath> paths,
                     @NonNull ServicePoint westNorthMapBound,
                     @NonNull ServicePoint eastSouthMapBound,
                     int maxMapZoom,
                     int minMapZoom) {
        this.uuid = uuid;
        this.name = name;
        //this.lastUpdateDatetime = lastUpdateDatetime;
        //this.createDatetime = createDatetime;
        //this.pubStatus = pubStatus;
        //this.price = price;
        //this.currency = currency;
        this.sights = sights;
        this.paths = paths;
        this.westNorthMapBound = westNorthMapBound;
        this.eastSouthMapBound = eastSouthMapBound;
        this.maxMapZoom = maxMapZoom;
        this.minMapZoom = minMapZoom;
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
        return paths.size();
    }

    public int getSightSize() {
        return sights.size();
    }

    @NonNull
    public ServicePath getPathAt(int index) {
        return paths.get(index);
    }

    @NonNull
    public Sight getSightAt(int index) {
        return sights.get(index);
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
        return minMapZoom;
    }

    public int getMaxMapZoom() {
        return maxMapZoom;
    }

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
