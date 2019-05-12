package com.example.win.easy.repository.web.callback;

import com.example.win.easy.repository.task.SongListBatchSyncTask;
import com.example.win.easy.repository.web.domain.NetworkSongList;
import com.example.win.easy.thread.AppExecutors;

import java.util.List;
import java.util.concurrent.Executor;

public class SongListBatchFetchCallBack extends BatchFetchCallBack<NetworkSongList> {

    private Executor executor;

    public SongListBatchFetchCallBack(){
        super();
        this.executor= AppExecutors.getInstance().diskIO();
    }
    @Override
    protected void update(List<NetworkSongList> newData) {
        executor.execute(new SongListBatchSyncTask(newData));
    }

}
