package com.example.win.easy.repository.web.callback;

import com.example.win.easy.application.SwiftSwitchApplication;
import com.example.win.easy.repository.task.SongListBatchSyncTask;

import java.util.List;
import java.util.concurrent.Executor;

public class SongListBatchFetchCallBack extends CustomCallBack {

    private Executor diskIO;

    public SongListBatchFetchCallBack(){
        super();
        this.diskIO= SwiftSwitchApplication.application.getAppComponent().diskIO();
    }

    @Override
    protected void process(Object data) {
        diskIO.execute(new SongListBatchSyncTask((List) data));
    }
}
