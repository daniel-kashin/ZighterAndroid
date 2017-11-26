package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;

public interface Validable<T extends Validable> {

    boolean isValid();

    /**
        @return null if unable to validate
     */
    T tryGetValidCopy();

    static void assertValid(@NonNull Validable validable) throws IllegalStateException {
        if (!validable.isValid()) throw new IllegalStateException();
    }

}
