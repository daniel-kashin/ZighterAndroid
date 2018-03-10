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

    // TODO
    //@SerializedName("")
    //private String description;

    // TODO
    //@SerializedName("type")
    //private String type;

    //@SerializedName("endpoint")
    //private List<ServicePoint> endpoints;

    //@SerializedName("endpoints")
    //private List<Integer> endpointIds;

    @SerializedName("points")
    private List<ServicePoint> points;

    public ServicePath(@NonNull String uuid,
                        //String type,
                        //List<ServicePoint> endpoints,
                        //List<Integer> endpointIds,
                        @NonNull List<ServicePoint> points) {
        this.uuid = uuid;
        this.points = points;
        //this.type = type;
    }

    @Override
    public boolean isValid() {
        if (uuid == null
                //|| endpoints == null || endpoints.size() != 2
                //|| endpointIds == null || endpointIds.size() != 2
                || points == null || points.size() < 2
            //|| endpointIds.get(0) >= points.size()
            //|| endpointIds.get(1) >= points.size()
                ) {
            return false;
        }

        //for (ServicePoint point : endpoints) {
        //    if (point == null || !point.isValid()) return false;
        //}
        for (ServicePoint point : points) {
            if (point == null || !point.isValid()) return false;
        }

        return true;
    }

    @Override
    @Nullable
    public ServicePath tryGetValid(boolean copy) {
        if (uuid == null
                //|| endpoints == null || endpoints.size() != 2
                //|| endpointIds == null || endpointIds.size() != 2
                || points == null || points.size() < 2) {
            return null;
        }

        //List<ServicePoint> endpointsCopy = new ArrayList<>();
        //for (ServicePoint point : endpoints) {
        //    if (point != null && point.isValid()) endpointsCopy.add(point);
        //}

        //List<Integer> endpointIdsCopy = new ArrayList<>();
        //endpointIdsCopy.addAll(endpointIds);

        List<ServicePoint> pointsCopy = new ArrayList<>();
        for (ServicePoint point : points) {
            if (point != null && point.isValid()) pointsCopy.add(point);
        }

        if (//endpointsCopy.size() != 2 ||
            //    endpointIdsCopy.size() != 2 ||
                pointsCopy.size() < 1) {
            return null;
        }

        if (!copy) {
            return this;
        }

        return new ServicePath(uuid,
                               //type,
                               //endpointsCopy,
                               //    endpointIdsCopy,
                               pointsCopy);
    }

    @NonNull
    public String getUuid() {
        return uuid;
    }

    //public String getTypeName() {
    //    return type;
    //}

    public int getPointSize() {
        return points.size();
    }

    public ServicePoint getPointAt(int index) {
        return points.get(index);
    }

    public ServicePoint getFirstEndpoint() {
        assertValid();
        return points.get(0);
    }

    public ServicePoint getSecondEndpoint() {
        assertValid();
        return points.get(points.size() - 1);
    }
}
