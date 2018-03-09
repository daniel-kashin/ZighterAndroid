package com.zighter.zighterandroid.data.entities.excursion;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zighter.zighterandroid.data.entities.service.ServicePath;
import com.zighter.zighterandroid.data.entities.service.ServicePoint;

import java.io.Serializable;
import java.util.List;

public class Excursion implements Serializable {
    @NonNull
    private final String uuid;
    @Nullable
    private final String name;
    @Nullable
    private final String lastUpdateDatetime;
    @Nullable
    private final String createDatetime;
    private final int pubStatus;
    private final double price;
    @Nullable
    private final String currency;
    private int userUuid;

    @NonNull
    private List<Sight> sights;
    @NonNull
    private List<ServicePath> paths;
    @Nullable
    private ServicePoint westNorthMapBound;
    @Nullable
    private ServicePoint eastSouthMapBound;
    private int maxMapZoom;
    private int minMapZoom;

    public Excursion(@NonNull String uuid,
                     @Nullable String name,
                     @Nullable String lastUpdateDatetime,
                     @Nullable String createDatetime,
                     int pubStatus,
                     double price,
                     @Nullable String currency,
                     int userUuid,
                     @NonNull List<Sight> sights,
                     @NonNull List<ServicePath> paths,
                     @Nullable ServicePoint westNorthMapBound,
                     @Nullable ServicePoint eastSouthMapBound,
                     int maxMapZoom,
                     int minMapZoom) {
        this.uuid = uuid;
        this.name = name;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.createDatetime = createDatetime;
        this.pubStatus = pubStatus;
        this.price = price;
        this.currency = currency;
        this.userUuid = userUuid;
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

    @Nullable
    public String getName() {
        return name;
    }

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

    public int getUserUuid() {
        return userUuid;
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

    @Nullable
    public ServicePoint getWestNorthMapBound() {
        return westNorthMapBound;
    }

    @Nullable
    public ServicePoint getEastSouthMapBound() {
        return eastSouthMapBound;
    }

    public int getMinMapZoom() {
        return minMapZoom;
    }

    public int getMaxMapZoom() {
        return maxMapZoom;
    }
}
