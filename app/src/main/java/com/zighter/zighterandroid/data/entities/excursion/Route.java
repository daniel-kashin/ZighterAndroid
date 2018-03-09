package com.zighter.zighterandroid.data.entities.excursion;

import android.support.annotation.NonNull;

import com.zighter.zighterandroid.data.entities.excursion.Sight;
import com.zighter.zighterandroid.data.entities.service.ServicePath;

import java.io.Serializable;
import java.util.List;

public class Route implements Serializable {
    @NonNull
    private List<ServicePath> paths;
    @NonNull
    private List<Sight> sights;

    private Route(@NonNull List<ServicePath> paths, @NonNull List<Sight> sights) {
        this.paths = paths;
        this.sights = sights;
    }

    int getPathSize() {
        return paths.size();
    }

    int getSightSize() {
        return sights.size();
    }

    @NonNull
    ServicePath getPathAt(int index) {
        return paths.get(index);
    }

    @NonNull
    Sight getSightAt(int index) {
        return sights.get(index);
    }
}
