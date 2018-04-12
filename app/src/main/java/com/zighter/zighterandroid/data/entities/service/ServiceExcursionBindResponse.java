package com.zighter.zighterandroid.data.entities.service;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class ServiceExcursionBindResponse {
    @SerializedName("excursion_id")
    String excursionUuid;

    public ServiceExcursionBindResponse(@Nullable String excursionUuid) {
        this.excursionUuid = excursionUuid;
    }

    @Nullable
    public String getExcursionUuid() {
        return excursionUuid;
    }
}
