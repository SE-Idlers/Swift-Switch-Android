package com.example.win.easy.download;

import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.web.callback.OnReadyFunc;

import java.util.concurrent.ThreadPoolExecutor;

public class DownloadService {

    private ThreadPoolExecutor downloadPoolExecutor;
    private FileService fileService;

    public DownloadService(ThreadPoolExecutor downloadPoolExecutor,FileService fileService){
        this.downloadPoolExecutor =downloadPoolExecutor;
        this.fileService=fileService;
    }

    public void download(SongDO songDO, OnReadyFunc<SongDO> onReadyFunc){
        //TODO 下载一首歌（相关的封面（头像）是扔在这里面一块儿下载还是单开一个都行）
    }

    public void download(SongListDO songListDO,OnReadyFunc<SongDO> onReadyFunc){
        //TODO 下载list封面（头像），现在是这样。虽然扔进来的参数是个完整的..
    }

//    private final int CORE_POOL_SIZE=5;
//    private final int MAX_POOL_SIZE=5;
//    private final int KEEP_ALIVE_TIME=50;
//    public DownloadService(){
//        downloadPoolExecutor=new ThreadPoolExecutor(
//                CORE_POOL_SIZE,
//                MAX_POOL_SIZE,
//                KEEP_ALIVE_TIME,
//                TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<>());
//    }
}
