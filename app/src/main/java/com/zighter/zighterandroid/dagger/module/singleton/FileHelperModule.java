package com.zighter.zighterandroid.dagger.module.singleton;

import android.content.Context;

import com.zighter.zighterandroid.data.file.FileHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class FileHelperModule {
    @Provides
    @Singleton
    FileHelper provideFileHelper(Context context) {
        return new FileHelper(context);
    }
}
