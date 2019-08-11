package com.example.win.easy.repository.task;

import com.example.win.easy.factory.AsyncTaskFactory;
import com.example.win.easy.factory.SongFactory;
import com.example.win.easy.repository.db.dao.SongDao;
import com.example.win.easy.repository.db.dao.SongListDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.data_object.SongListDO;
import com.example.win.easy.repository.db.data_object.SongDO;
import com.example.win.easy.repository.db.data_object.SongXSongListDO;
import com.example.win.easy.repository.web.dto.SongDTO;
import com.example.win.easy.repository.web.dto.SongListDTO;
import com.example.win.easy.repository.web.download.PictureDownloadManager;
import com.example.win.easy.repository.web.download.SongDownloadManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Builder;

@Builder
public class SongListBatchSyncTask implements Runnable {

    private SongDao songDao;
    private SongListDao songListDao;
    private SongXSongListDao songXSongListDao;
    private SongDownloadManager songDownloadManager;
    private PictureDownloadManager pictureDownloadManager;
    private SongFactory songFactory;
    private AsyncTaskFactory asyncTaskFactory;

    private List<SongListDTO> songListDTOs;

    private List<SongDO> songLocalRecords;
    private List<SongDTO> songDTOs;
    private List<Long> songIds;
    private List<Long> songListIds;
    private List<SongXSongListDO> relations;


    @Override
    public void run() {
        updateSongs();
        updateSongLists();
        updateSongXSongLists();
        initiateDownloadTask();
    }

    private void updateSongs(){
        //去重
        Set<SongDTO> uniqueSongDTOs =new HashSet<>();
        for (SongListDTO songListDTO : songListDTOs)
            uniqueSongDTOs.addAll(songListDTO.getSongDTOs());
        for (SongDTO songDTO : uniqueSongDTOs){
            //查找本地记录
            SongDO localRecord= songDao.findLocalRecordOfNetworkSong(
                    songDTO.totalName,
                    songDTO.author,
                    songDTO.source.toString(),
                    songDTO.uid,
                    songDTO.remoteId);
            //没有本地记录时插入
            if (localRecord==null) {
                localRecord=songFactory.create(songDTO);
                songIds.add(songDao.insert(localRecord));
            } else//有记录时记下id
                songIds.add(localRecord.id);
            //保留网络歌曲的引用
            songDTOs.add(songDTO);
            //保留本地歌曲的引用
            songLocalRecords.add(localRecord);
        }
    }

    private void updateSongLists(){
        for (SongListDTO songListDTO : songListDTOs){
            //查找本地记录
            SongListDO localRecord= songListDao.findLocalRecordOfNetworkSongList(
                    songListDTO.name,
                    songListDTO.source.toString(),
                    songListDTO.uid,
                    songListDTO.remoteId);
            //没有本地记录时插入
            if (localRecord==null){
                localRecord=new SongListDO(songListDTO);
                songListIds.add(songListDao.insert(localRecord));
            }else//有记录时记下id
                songListIds.add(localRecord.id);
        }
    }

    private void updateSongXSongLists(){
        for (SongListDTO songListDTO : songListDTOs){
            //获取歌单id
            long songListId=songListIds.get(songListDTOs.indexOf(songListDTO));
            List<SongDTO> contents= songListDTO.songDTOs;
            for (SongDTO songDTO :contents){
                //获取歌曲id
                long songId=songIds.get(songDTOs.indexOf(songDTO));
                //添加 歌曲-歌单 关系记录
                relations.add(new SongXSongListDO(songId,songListId));
            }
        }
        //插入关系数据
        songXSongListDao.insert(relations);
    }

    private void initiateDownloadTask(){
        int songAmount=songLocalRecords.size();
        for (int index=0;index<songAmount;index++){
            SongDTO songDTO = songDTOs.get(index);
            long songId=songIds.get(index);
            songDownloadManager.download(asyncTaskFactory.create(songDTO,songId,SongDownloadTask.class));
            pictureDownloadManager.download(asyncTaskFactory.create(songDTO,songId,SongPictureDownloadTask.class));
        }

        int songListAmount=songListIds.size();
        for (int index=0;index<songListAmount;index++){
            SongListDTO songListDTO = songListDTOs.get(index);
            long songListId=songListIds.get(index);
            pictureDownloadManager.download(new SongListPictureDownloadTask(songListDTO,songListId));
        }

    }
}
