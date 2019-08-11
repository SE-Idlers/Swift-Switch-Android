package com.example.win.easy.factory;

import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.db.dao.SongListDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.task.DownloadTask;
import com.example.win.easy.repository.task.SongDownloadTask;
import com.example.win.easy.repository.task.SongListBatchSyncTask;
import com.example.win.easy.repository.task.SongPictureDownloadTask;
import com.example.win.easy.repository.web.DownloadFilenameResolver;
import com.example.win.easy.repository.web.dto.SongDTO;
import com.example.win.easy.repository.web.dto.SongListDTO;
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
    private SongDao songDao;
    private SongListDao songListDao;
    private SongXSongListDao songXSongListDao;
    private SongDownloadManager songDownloadManager;
    private PictureDownloadManager pictureDownloadManager;
    private SongFactory songFactory;

    @Inject
    public AsyncTaskFactory(DownloadFilenameResolver resolver,
                            SongDao songDao,
                            SongListDao songListDao,
                            SongXSongListDao songXSongListDao,
                            SongDownloadManager songDownloadManager,
                            PictureDownloadManager pictureDownloadManager,
                            SongFactory songFactory){
        this.resolver=resolver;
        this.songDao = songDao;
        this.songListDao = songListDao;
        this.songXSongListDao=songXSongListDao;
        this.songDownloadManager=songDownloadManager;
        this.pictureDownloadManager=pictureDownloadManager;
        this.songFactory=songFactory;
    }

    public DownloadTask create(SongDTO songDTO, long songId, Class<? extends DownloadTask> taskClass){
        if (taskClass==SongDownloadTask.class){

            String tempName=resolver.tempSongFilePath(songDTO);
            String finishName=resolver.finishSongFilePath(songDTO);

            return SongDownloadTask.builder()
                    .songDTO(songDTO)
                    .songId(songId)
                    .tempName(tempName)
                    .finishName(finishName)
                    .tempFile(new File(tempName))
                    .finishFile(new File(finishName))
                    .songDao(songDao)
                    .build();
        }else if (taskClass== SongPictureDownloadTask.class){

            String tempName=resolver.tempPictureFilePath(songDTO);
            String finishName=resolver.finishPictureFilePath(songDTO);

            return SongPictureDownloadTask.builder()
                    .songDTO(songDTO)
                    .songId(songId)
                    .tempName(tempName)
                    .finishName(finishName)
                    .tempFile(new File(tempName))
                    .finishFile(new File(finishName))
                    .songDao(songDao)
                    .build();
        }
        return null;
    }

    public SongListBatchSyncTask create(List<LinkedTreeMap<String,Object>> networkNewData){

        List<SongListDTO> songListDTOs =new ArrayList<>();
        for (LinkedTreeMap<String,Object> treeMap:networkNewData)
            songListDTOs.add(new SongListDTO(treeMap));

        return SongListBatchSyncTask.builder()
                .songListDTOs(songListDTOs)
                .songDao(songDao)
                .songListDao(songListDao)
                .songXSongListDao(songXSongListDao)
                .songDownloadManager(songDownloadManager)
                .pictureDownloadManager(pictureDownloadManager)
                .songFactory(songFactory)
                .asyncTaskFactory(this)
                .songLocalRecords(new ArrayList<>())
                .songDTOs(new ArrayList<>())
                .songIds(new ArrayList<>())
                .songListIds(new ArrayList<>())
                .relations(new ArrayList<>())
                .build();
    }

}
