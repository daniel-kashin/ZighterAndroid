package com.zighter.zighterandroid.data.entities.excursion;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ServiceRoute extends Validable<ServiceRoute> {
    @SerializedName("paths")
    private List<ServicePath> paths;

    @SerializedName("sights")
    private List<ServiceSight> sights;

    private ServiceRoute(@NonNull List<ServicePath> paths, @NonNull List<ServiceSight> sights) {
        this.paths = paths;
        this.sights = sights;
    }

    @Override
    boolean isValid() {
        // TODO
        //if ((paths == null || paths.size() == 0) && (sights == null || sights.size() == 0)) {
        //    return false;
        //}

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
    ServiceRoute tryGetValid(boolean copy) {
        // TODO
        //if ((paths == null || paths.size() == 0) && (sights == null || sights.size() == 0)) {
        //    return null;
        //}

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

        // TODO
        //if (pathsCopy.size() == 0 || sightsCopy.size() == 0) {
        //    return null;
        //}

        if (!copy) {
            return this;
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
