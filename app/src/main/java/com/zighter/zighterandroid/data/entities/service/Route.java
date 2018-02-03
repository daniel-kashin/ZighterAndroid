package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.zighter.zighterandroid.R;

import java.util.ArrayList;
import java.util.List;

public class Route extends Validable<Route> {
    @SerializedName("paths")
    private List<Path> paths;

    @SerializedName("sights")
    private List<Sight> sights;

    private Route(@NonNull List<Path> paths, @NonNull List<Sight> sights) {
        this.paths = paths;
        this.sights = sights;
    }

    @Override
    public boolean isValid() {
        if ((paths == null || paths.size() == 0) && (sights == null || sights.size() == 0)) {
            return false;
        }

        if (paths != null) {
            for (Path path : paths) {
                if (path == null || !path.isValid()) return false;
            }
        }
        if (sights != null) {
            for (Sight sight : sights) {
                if (sight == null || !sight.isValid()) return false;
            }
        }
        return true;
    }

    @Override
    public Route tryGetValidCopy() {
        if ((paths == null || paths.size() == 0) && (sights == null || sights.size() == 0)) {
            return null;
        }

        List<Path> pathsCopy = new ArrayList<>();
        if (paths != null) {
            for (Path path : paths) {
                if (path != null && path.isValid()) pathsCopy.add(path);
            }
        }

        List<Sight> sightsCopy = new ArrayList<>();
        if (sights != null) {
            for (Sight sight : sights) {
                if (sight != null && sight.isValid()) sightsCopy.add(sight);
            }
        }

        if (paths.size() != 0 || sights.size() != 0) {
            return new Route(pathsCopy, sightsCopy);
        } else {
            return null;
        }
    }

    int getPathSize() {
        return paths == null ? 0 : paths.size();
    }

    int getSightSize() {
        return sights == null ? 0 : sights.size();
    }

    @NonNull
    Path getPathAt(int index) {
        return paths.get(index);
    }

    @NonNull
    Sight getSightAt(int index) {
        return sights.get(index);
    }
}
