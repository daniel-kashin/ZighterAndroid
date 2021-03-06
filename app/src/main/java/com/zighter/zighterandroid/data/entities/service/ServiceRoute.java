package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ServiceRoute extends Validable<ServiceRoute> {
    @SerializedName("paths")
    private List<ServicePath> paths;

    @SerializedName("sights")
    private List<ServiceSight> sights;

    @VisibleForTesting
    public ServiceRoute(@NonNull List<ServicePath> paths, @NonNull List<ServiceSight> sights) {
        this.paths = paths;
        this.sights = sights;
    }

    @Override
    public boolean isValid() {
        if (paths != null) {
            for (ServicePath path : paths) {
                if (path == null || !path.isValid()) return false;
            }
        }
        if (sights != null) {
            for (ServiceSight sight : sights) {
                if (sight == null || !sight.isValid()) return false;
            }
        }
        return true;
    }

    @Override
    public ServiceRoute tryGetValid(boolean copy) {
        List<ServicePath> pathsCopy = new ArrayList<>();
        if (paths != null) {
            for (ServicePath path : paths) {
                if (path != null && path.isValid()) pathsCopy.add(path);
            }
        }

        List<ServiceSight> sightsCopy = new ArrayList<>();
        if (sights != null) {
            for (ServiceSight sight : sights) {
                if (sight != null && sight.isValid()) sightsCopy.add(sight);
            }
        }

        return new ServiceRoute(pathsCopy, sightsCopy);
    }

    int getPathSize() {
        return paths == null ? 0 : paths.size();
    }

    int getSightSize() {
        return sights == null ? 0 : sights.size();
    }

    @NonNull
    ServicePath getPathAt(int index) {
        return paths.get(index);
    }

    @NonNull
    ServiceSight getSightAt(int index) {
        return sights.get(index);
    }
}
