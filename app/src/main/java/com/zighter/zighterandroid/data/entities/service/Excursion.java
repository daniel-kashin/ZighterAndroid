package com.zighter.zighterandroid.data.entities.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;


public class Excursion implements Validable<Excursion> {

    @SerializedName("id")
    private String uuid;

    @SerializedName("name")
    private String name;

    @SerializedName("last_update")
    private String lastUpdateDatetime;

    @SerializedName("timestamp")
    private String createDatetime;

    @SerializedName("pub_status")
    private int pubStatus;

    @SerializedName("price")
    private double price;

    @SerializedName("currency")
    private String currency;

    @SerializedName("user")
    private int userUuid;

    @SerializedName("route")
    private Route route;

    private Excursion(@NonNull String uuid, @Nullable String name, @Nullable String lastUpdateDatetime,
                     String createDatetime, int pubStatus, double price, String currency, int userUuid,
                     Route route) {
        this.uuid = uuid;
        this.name = name;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.createDatetime = createDatetime;
        this.pubStatus = pubStatus;
        this.price = price;
        this.currency = currency;
        this.userUuid = userUuid;
        this.route = route;
    }

    public Excursion replaceRoute(Route route) {
        return new Excursion(uuid, name, lastUpdateDatetime, createDatetime, pubStatus,
                price, currency, userUuid, route);
    }

    public String getUuid() {
        Validable.assertValid(this);
        return uuid;
    }

    public String getName() {
        Validable.assertValid(this);
        return name;
    }

    public String getLastUpdateDatetime() {
        Validable.assertValid(this);
        return lastUpdateDatetime;
    }

    public String getCreateDatetime() {
        Validable.assertValid(this);
        return createDatetime;
    }

    public int getPubStatus() {
        Validable.assertValid(this);
        return pubStatus;
    }

    public double getPrice() {
        Validable.assertValid(this);
        return price;
    }

    public String getCurrency() {
        Validable.assertValid(this);
        return currency;
    }

    public int getUserUuid() {
        Validable.assertValid(this);
        return userUuid;
    }

    public int getPathSize() {
        Validable.assertValid(this);
        return route.getPathSize();
    }

    public int getSightSize() {
        Validable.assertValid(this);
        return route.getSightSize();
    }

    @NonNull
    public Path getPathAt(int index) {
        Validable.assertValid(this);
        return route.getPathAt(index);
    }

    @NonNull
    public Sight getSightAt(int index) {
        Validable.assertValid(this);
        return route.getSightAt(index);
    }

    @Override
    public boolean isValid() {
        return userUuid != 0 && uuid != null && !uuid.equals("") && route != null && route.isValid();
    }

    @Override
    public Excursion tryGetValidCopy() {
        if (route == null || userUuid == 0 || uuid == null || uuid.equals("")) {
            return null;
        }

        Route routeCopy = route.tryGetValidCopy();
        if (routeCopy != null) {
            return new Excursion(uuid, name, lastUpdateDatetime, createDatetime, pubStatus,
                                 price, currency, userUuid, routeCopy);
        } else {
            return null;
        }
    }
}
