package com.zighter.zighterandroid.data.exception;

import android.support.annotation.Nullable;

public class ServerNotAuthorizedException extends ServerException {
    public ServerNotAuthorizedException(@Nullable Throwable origin) {
        super(origin);
    }
}
