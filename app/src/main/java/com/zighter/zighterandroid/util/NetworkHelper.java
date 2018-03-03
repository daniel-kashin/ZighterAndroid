package com.zighter.zighterandroid.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

public class NetworkHelper {
    @NonNull
    private NetworkStatus getStatus(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return NetworkStatus.UNKNOWN;
        }

        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return NetworkStatus.WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                return NetworkStatus.MOBILE;
            } else {
                return NetworkStatus.DISCONNECTED;
            }
        } else {
            return NetworkStatus.DISCONNECTED;
        }
    }

    public enum NetworkStatus {
        UNKNOWN,
        WIFI,
        MOBILE,
        DISCONNECTED
    }
}
