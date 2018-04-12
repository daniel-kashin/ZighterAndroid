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

    @NonNull
    private List<Sight> sights;

    @NonNull
    private List<Path> paths;

    @NonNull
    private Point westNorthMapBound;
    @NonNull
    private Point eastSouthMapBound;

    private int maxMapZoom;
    private int minMapZoom;

    public Excursion(@NonNull String uuid,
                     @NonNull String name,
                     @NonNull List<Sight> sights,
                     @NonNull List<Path> paths,
                     @NonNull Point westNorthMapBound,
                     @NonNull Point eastSouthMapBound,
                     int maxMapZoom,
                     int minMapZoom) {
        this.uuid = uuid;
        this.name = name;
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
    public Path getPathAt(int index) {
        return paths.get(index);
    }

    @NonNull
    public Sight getSightAt(int index) {
        return sights.get(index);
    }

    @NonNull
    public Point getWestNorthMapBound() {
        return westNorthMapBound;
    }

    @NonNull
    public Point getEastSouthMapBound() {
        return eastSouthMapBound;
    }

    public int getMinMapZoom() {
        return minMapZoom;
    }

    public int getMaxMapZoom() {
        return maxMapZoom;
    }
}
