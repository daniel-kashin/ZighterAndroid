package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;

import javax.annotation.Nullable;

public class ServiceSearchSort {
    @Nullable
    public final SortType sortType;
    @Nullable
    public final SortOrder sortOrder;

    public ServiceSearchSort(@Nullable SortType sortType, @Nullable SortOrder sortOrder) {
        this.sortOrder = sortOrder;
        this.sortType = sortType;
    }

    @Nullable
    public SortType getSortType() {
        return sortType;
    }

    @Nullable
    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public enum SortType {
        DATE,
        PRICE
    }

    public enum SortOrder {
        ASC,
        DESC
    }
}
