package com.zighter.zighterandroid.data.exception;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ServerLoginException extends ServerException {
    public ServerLoginException(@Nullable Throwable origin) {
        super(origin);
    }
}
