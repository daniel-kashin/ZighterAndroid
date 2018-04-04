package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class ServicePath extends Validable<ServicePath> {
    @SerializedName("id")
    private String uuid;

    @SerializedName("points")
    private List<ServicePoint> points;

    public ServicePath(@NonNull String uuid,
                       @NonNull List<ServicePoint> points) {
        this.uuid = uuid;
        this.points = points;
    }

    @Override
    public boolean isValid() {
        if (uuid == null || points == null || points.size() < 2) {
            return false;
        }

        for (ServicePoint point : points) {
            if (point == null || !point.isValid()) return false;
        }

        return true;
    }

    @Override
    @Nullable
    public ServicePath tryGetValid(boolean copy) {
        if (uuid == null || points == null || points.size() < 2) {
            return null;
        }

        List<ServicePoint> pointsCopy = new ArrayList<>();
        for (ServicePoint point : points) {
            if (point != null && point.isValid()) pointsCopy.add(point);
        }

        if (pointsCopy.size() < 1) {
            return null;
        }

        if (!copy) {
            return this;
        }

        return new ServicePath(uuid, pointsCopy);
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public int getPointSize() {
        return points.size();
    }

    public ServicePoint getPointAt(int index) {
        return points.get(index);
    }

    public ServicePoint getFirstEndpoint() {
        return points.get(0);
    }

    public ServicePoint getSecondEndpoint() {
        return points.get(points.size() - 1);
    }
}
