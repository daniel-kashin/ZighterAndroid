package com.zighter.zighterandroid.data.exception;

import android.support.annotation.Nullable;

public class ServerNoAccessException extends ServerException {
    public ServerNoAccessException(@Nullable Throwable origin) {
        super(origin);
    }
}
