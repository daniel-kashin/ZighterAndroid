package com.zighter.zighterandroid.data.entities.service;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Sight implements Validable<Sight>, Serializable {

    @SerializedName("id")
    private String uuid;

    @SerializedName("point")
    private Point point;

    // TODO
    //@SerializedName("")
    //private String description;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    private Sight(@NonNull String uuid, @NonNull Point point, @Nullable String name, @Nullable String type) {
        this.uuid = uuid;
        this.point = point;
        this.name = name;
        this.type = type;
    }

    @Override
    public boolean isValid() {
        return point != null && point.isValid() && uuid != null && !uuid.equals("");
    }

    @Override
    public Sight tryGetValidCopy() {
        if (point == null || uuid == null || uuid.equals("")) return null;

        Point pointCopy = point.tryGetValidCopy();
        if (pointCopy != null) {
            return new Sight(uuid, point.tryGetValidCopy(), name, type);
        } else {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUuid() {
        return uuid;
    }

    public double getLongitude() {
        return point.getLongitude();
    }

    public double getLatitude() {
        return point.getLatitude();
    }

}
