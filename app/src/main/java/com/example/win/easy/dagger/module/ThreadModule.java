package com.example.win.easy.dagger.module;

import com.example.win.easy.thread.AppExecutors;

import java.util.concurrent.Executor;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ThreadModule {

    @Provides
    @Singleton
    @Named("dbAccess") static Executor provideDiskIO(AppExecutors appExecutors){
        return appExecutors.diskIO();
    }

    @Provides
    @Singleton
    @Named("mainThread") static Executor provideMainThread(AppExecutors appExecutors){
        return appExecutors.mainThread();
    }
}
