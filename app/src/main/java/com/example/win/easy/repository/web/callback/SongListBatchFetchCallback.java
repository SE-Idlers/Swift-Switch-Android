package com.example.win.easy.repository.web.callback;

import com.example.win.easy.factory.AsyncTaskFactory;

import java.util.List;
import java.util.concurrent.Executor;

import lombok.Builder;

@Builder
public class SongListBatchFetchCallback extends CustomCallback {

    private Executor diskIO;
    private AsyncTaskFactory asyncTaskFactory;

    @Override
    protected void process(Object data) {
        diskIO.execute(asyncTaskFactory.create((List) data));
    }
}
