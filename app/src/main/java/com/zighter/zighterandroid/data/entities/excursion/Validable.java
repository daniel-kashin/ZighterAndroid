package com.zighter.zighterandroid.data.entities.excursion;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zighter.zighterandroid.data.exception.ServerResponseValidationException;

import java.io.Serializable;

abstract class Validable<T extends Validable> implements Serializable {

    abstract boolean isValid();

    /**
     * @return null if unable to validate
     */
    @Nullable
    abstract T tryGetValid(boolean copy);

    void assertValid() throws IllegalStateException {
        if (!isValid()) {
            throw new IllegalStateException("Is not valid");
        }
    }

    @NonNull
    T tryGetValidOrThrowException() throws ServerResponseValidationException {
        T valid = this.tryGetValid(false);
        if (valid != null) {
            return valid;
        } else {
            throw new ServerResponseValidationException();
        }
    }
}
