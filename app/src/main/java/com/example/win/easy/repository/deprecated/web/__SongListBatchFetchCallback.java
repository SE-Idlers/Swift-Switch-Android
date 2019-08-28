package com.example.win.easy.repository.deprecated.web;

import com.example.win.easy.factory.AsyncTaskFactory;
import com.example.win.easy.repository.web.callback.OnReadyFunc;

import java.util.List;
import java.util.concurrent.Executor;

import lombok.Builder;

@Builder
public class __SongListBatchFetchCallback extends __CustomCallback {

    private Executor diskIO;
    private AsyncTaskFactory asyncTaskFactory;

    @Override
    protected void process(Object data) {
        diskIO.execute(asyncTaskFactory.create((List) data));
    }

    public void process(OnReadyFunc<String> func){

    }
}
