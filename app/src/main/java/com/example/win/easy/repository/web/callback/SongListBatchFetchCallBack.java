package com.example.win.easy.repository.web.callback;

import com.example.win.easy.repository.task.SongListBatchSyncTask;
import com.example.win.easy.repository.web.domain.NetworkSongList;

import java.util.List;
import java.util.concurrent.Executor;

public class SongListBatchFetchCallBack extends BatchFetchCallBack<NetworkSongList> {

    private Executor executor;

    public SongListBatchFetchCallBack(Executor executor){
        super();
        this.executor=executor;
    }
    @Override
    protected void update(List<NetworkSongList> newData) {
        executor.execute(new SongListBatchSyncTask(newData));
    }

}
