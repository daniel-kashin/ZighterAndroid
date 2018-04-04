package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ServiceSearchExcursions extends Validable<ServiceSearchExcursions> {
    @SerializedName("results")
    private List<ServiceSearchExcursion> excursions;

    private ServiceSearchExcursions(@NonNull List<ServiceSearchExcursion> excursions) {
        this.excursions = excursions;
    }

    @NonNull
    public List<ServiceSearchExcursion> getExcursions() {
        if (!isValid()) {
            throw new IllegalStateException();
        }
        return excursions;
    }

    @Override
    public boolean isValid() {
        if (excursions == null) {
            return false;
        }

        for (ServiceSearchExcursion serviceSearchExcursion : excursions) {
            if (!serviceSearchExcursion.isValid()) {
                return false;
            }
        }

        return true;
    }

    @Nullable
    @Override
    public ServiceSearchExcursions tryGetValid(boolean copy) {
        List<ServiceSearchExcursion> valid = new ArrayList<>();

        if (excursions != null) {
            for (ServiceSearchExcursion serviceSearchExcursion : excursions) {
                if (serviceSearchExcursion.isValid()) {
                    valid.add(serviceSearchExcursion);
                }
            }
        }

        return new ServiceSearchExcursions(valid);
    }
}
