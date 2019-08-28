package com.example.win.easy.factory;

import com.example.win.easy.repository.deprecated.web.__SongListBatchFetchCallback;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class CallbackFactory {

    private AsyncTaskFactory asyncTaskFactory;
    private Executor diskIO;

    @Inject
    public CallbackFactory(AsyncTaskFactory asyncTaskFactory,
                           @Named("dbAccess") Executor diskIO){
        this.asyncTaskFactory=asyncTaskFactory;
        this.diskIO=diskIO;
    }

    public __SongListBatchFetchCallback create(){
        return __SongListBatchFetchCallback.builder()
                .asyncTaskFactory(asyncTaskFactory)
                .diskIO(diskIO)
                .build();
    }


}
