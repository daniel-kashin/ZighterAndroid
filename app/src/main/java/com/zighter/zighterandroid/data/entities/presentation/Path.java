package com.zighter.zighterandroid.data.entities.presentation;

import android.support.annotation.NonNull;
import com.zighter.zighterandroid.data.entities.service.ServicePoint;

import java.util.ArrayList;
import java.util.List;

public class Path {
    @NonNull
    private String uuid;
    @NonNull
    private List<Point> points;

    public Path(@NonNull String uuid,
                @NonNull List<Point> points) {
        this.uuid = uuid;
        this.points = points;
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    public int getPointSize() {
        return points.size();
    }

    public Point getPointAt(int index) {
        return points.get(index);
    }

    public Point getFirstEndpoint() {
        return points.get(0);
    }

    public Point getSecondEndpoint() {
        return points.get(points.size() - 1);
    }
}
