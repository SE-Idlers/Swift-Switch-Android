package com.example.win.easy.factory;

import com.example.win.easy.repository.web.callback.SongListBatchFetchCallback;

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

    public SongListBatchFetchCallback create(){
        return SongListBatchFetchCallback.builder()
                .asyncTaskFactory(asyncTaskFactory)
                .diskIO(diskIO)
                .build();
    }

}
