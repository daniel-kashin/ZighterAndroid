package com.zighter.zighterandroid.data.exception;


public abstract class BaseException extends Exception {

    private final Throwable original;

    BaseException(Throwable original) {
        this.original = original;
    }

    public Throwable getOriginal() {
        return original;
    }

}
