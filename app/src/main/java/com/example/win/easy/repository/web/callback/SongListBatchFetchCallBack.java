package com.example.win.easy.repository.web.callback;

import com.example.win.easy.repository.task.SongListBatchSyncTask;
import com.example.win.easy.thread.AppExecutors;

import java.util.List;
import java.util.concurrent.Executor;

public class SongListBatchFetchCallBack extends CustomCallBack {

    private Executor executor;

    public SongListBatchFetchCallBack(){
        super();
        this.executor= AppExecutors.getInstance().diskIO();
    }

    @Override
    protected void process(Object data) {
        executor.execute(new SongListBatchSyncTask((List) data));
    }
}
