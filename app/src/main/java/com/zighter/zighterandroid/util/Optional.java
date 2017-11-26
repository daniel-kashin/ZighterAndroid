package com.zighter.zighterandroid.util;

public class Optional<T> {

    private final T value;

    private Optional(T value) {
        this.value = value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public T get() {
        return value;
    }

    public static <T> Optional<T> of(T value) {
        return new Optional<T>(value);
    }

}