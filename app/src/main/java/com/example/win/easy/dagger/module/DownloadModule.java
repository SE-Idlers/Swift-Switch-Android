package com.example.win.easy.dagger.module;

import android.os.Environment;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class DownloadModule {

    @Provides @Named("downloadRootDir")static String provideDownloadRootDir(){
        return Environment.getExternalStorageDirectory().getAbsolutePath()+"/Swift Switch";
    }

    @Provides @Named("suffixOfTempFile")static String provideSuffixOfTempFile(){
        return "~TEMP";
    }
}
