package com.example.win.easy.factory;

import com.example.win.easy.repository.db.dao.SongListPojoDao;
import com.example.win.easy.repository.db.dao.SongPojoDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.task.DownloadTask;
import com.example.win.easy.repository.task.SongDownloadTask;
import com.example.win.easy.repository.task.SongListBatchSyncTask;
import com.example.win.easy.repository.task.SongPictureDownloadTask;
import com.example.win.easy.repository.web.DownloadFilenameResolver;
import com.example.win.easy.repository.web.domain.NetworkSong;
import com.example.win.easy.repository.web.domain.NetworkSongList;
import com.example.win.easy.repository.web.download.PictureDownloadManager;
import com.example.win.easy.repository.web.download.SongDownloadManager;
import com.google.gson.internal.LinkedTreeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AsyncTaskFactory {

    private DownloadFilenameResolver resolver;
    private SongPojoDao songPojoDao;
    private SongListPojoDao songListPojoDao;
    private SongXSongListDao songXSongListDao;
    private SongDownloadManager songDownloadManager;
    private PictureDownloadManager pictureDownloadManager;
    private SongFactory songFactory;

    @Inject
    public AsyncTaskFactory(DownloadFilenameResolver resolver,
                            SongPojoDao songPojoDao,
                            SongListPojoDao songListPojoDao,
                            SongXSongListDao songXSongListDao,
                            SongDownloadManager songDownloadManager,
                            PictureDownloadManager pictureDownloadManager,
                            SongFactory songFactory){
        this.resolver=resolver;
        this.songPojoDao=songPojoDao;
        this.songListPojoDao=songListPojoDao;
        this.songXSongListDao=songXSongListDao;
        this.songDownloadManager=songDownloadManager;
        this.pictureDownloadManager=pictureDownloadManager;
        this.songFactory=songFactory;
    }

    public DownloadTask create(NetworkSong networkSong, long songId,Class<? extends DownloadTask> taskClass){
        if (taskClass==SongDownloadTask.class){

            String tempName=resolver.tempSongFilePath(networkSong);
            String finishName=resolver.finishSongFilePath(networkSong);

            return SongDownloadTask.builder()
                    .networkSong(networkSong)
                    .songId(songId)
                    .tempName(tempName)
                    .finishName(finishName)
                    .tempFile(new File(tempName))
                    .finishFile(new File(finishName))
                    .songPojoDao(songPojoDao)
                    .build();
        }else if (taskClass== SongPictureDownloadTask.class){

            String tempName=resolver.tempPictureFilePath(networkSong);
            String finishName=resolver.finishPictureFilePath(networkSong);

            return SongPictureDownloadTask.builder()
                    .networkSong(networkSong)
                    .songId(songId)
                    .tempName(tempName)
                    .finishName(finishName)
                    .tempFile(new File(tempName))
                    .finishFile(new File(finishName))
                    .songPojoDao(songPojoDao)
                    .build();
        }
        return null;
    }

    public SongListBatchSyncTask create(List<LinkedTreeMap<String,Object>> networkNewData){

        List<NetworkSongList> networkSongLists=new ArrayList<>();
        for (LinkedTreeMap<String,Object> treeMap:networkNewData)
            networkSongLists.add(new NetworkSongList(treeMap));

        return SongListBatchSyncTask.builder()
                .networkSongLists(networkSongLists)
                .songPojoDao(songPojoDao)
                .songListPojoDao(songListPojoDao)
                .songXSongListDao(songXSongListDao)
                .songDownloadManager(songDownloadManager)
                .pictureDownloadManager(pictureDownloadManager)
                .songFactory(songFactory)
                .asyncTaskFactory(this)
                .songLocalRecords(new ArrayList<>())
                .networkSongs(new ArrayList<>())
                .songIds(new ArrayList<>())
                .songListIds(new ArrayList<>())
                .relations(new ArrayList<>())
                .build();
    }

}
