package com.zighter.zighterandroid.data.entities.service;

import java.io.Serializable;

abstract class Validable<T extends Validable> implements Serializable {

    abstract boolean isValid();

    /**
     @return null if unable to validate
     */
    abstract T tryGetValidCopy();

    void assertValid() throws IllegalStateException {
        if (!isValid()) {
            throw new IllegalStateException("Is not valid");
        }
    }

}
