package com.zighter.zighterandroid.data.entities.presentation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zighter.zighterandroid.data.entities.service.ServiceGuide;

import java.io.Serializable;

public class ExcursionWithGuide implements Serializable {
    @NonNull
    private final Excursion excursion;

    @Nullable
    private final Guide guide;

    public ExcursionWithGuide(@NonNull Excursion excursion,
                              @Nullable Guide guide) {
        this.excursion = excursion;
        this.guide = guide;
    }

    @NonNull
    public Excursion getExcursion() {
        return excursion;
    }

    @Nullable
    public Guide getGuide() {
        return guide;
    }
}
