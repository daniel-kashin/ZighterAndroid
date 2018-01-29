package com.zighter.zighterandroid.data.entities.service;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public class Path extends Validable<Path> {

    @SerializedName("id")
    private int uuid;

    // TODO
    //@SerializedName("")
    //private String description;

    @SerializedName("type")
    private String type;

    //@SerializedName("endpoint")
    //private List<Point> endpoints;

    //@SerializedName("endpoints")
    //private List<Integer> endpointIds;

    @SerializedName("points")
    private List<Point> points;

    private Path(int uuid, String type,
                 //List<Point> endpoints,
                 //List<Integer> endpointIds,
                 List<Point> points) {
        this.uuid = uuid;
        this.type = type;
    }

    @Override
    public boolean isValid() {
        if (uuid == 0
                //|| endpoints == null || endpoints.size() != 2
                //|| endpointIds == null || endpointIds.size() != 2
                || points == null || points.size() < 1
            //|| endpointIds.get(0) >= points.size()
            //|| endpointIds.get(1) >= points.size()
                ) {
            return false;
        }

        //for (Point point : endpoints) {
        //    if (point == null || !point.isValid()) return false;
        //}
        for (Point point : points) {
            if (point == null || !point.isValid()) return false;
        }

        return true;
    }

    @Override
    public Path tryGetValidCopy() {
        if (uuid == 0
                //|| endpoints == null || endpoints.size() != 2
                //|| endpointIds == null || endpointIds.size() != 2
                || points == null || points.size() < 2) {
            return null;
        }

        //List<Point> endpointsCopy = new ArrayList<>();
        //for (Point point : endpoints) {
        //    if (point != null && point.isValid()) endpointsCopy.add(point);
        //}

        //List<Integer> endpointIdsCopy = new ArrayList<>();
        //endpointIdsCopy.addAll(endpointIds);

        List<Point> pointsCopy = new ArrayList<>();
        for (Point point : points) {
            if (point != null && point.isValid()) pointsCopy.add(point);
        }

        if (//endpointsCopy.size() == 2 &&
            //    endpointIdsCopy.size() == 2 &&
                pointsCopy.size() >= 1) {
            return new Path(uuid,
                            type,
                            //endpointsCopy,
                            //    endpointIdsCopy,
                            pointsCopy);
        } else {
            return null;
        }
    }

    public int getUuid() {
        return uuid;
    }

    public String getType() {
        return type;
    }

    public int getPointSize() {
        return points.size();
    }

    public Point getPointAt(int index) {
        return points.get(index);
    }

    public Point getFirstEndpoint() {
        assertValid();
        return points.get(0);
    }

    public Point getSecondEndpoint() {
        assertValid();
        return points.get(points.size() - 1);
    }
}
