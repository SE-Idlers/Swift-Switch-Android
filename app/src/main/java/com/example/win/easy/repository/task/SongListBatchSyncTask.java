package com.example.win.easy.repository.task;

import com.example.win.easy.factory.AsyncTaskFactory;
import com.example.win.easy.factory.SongFactory;
import com.example.win.easy.repository.db.dao.SongListPojoDao;
import com.example.win.easy.repository.db.dao.SongPojoDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.db.pojo.SongXSongList;
import com.example.win.easy.repository.web.domain.NetworkSong;
import com.example.win.easy.repository.web.domain.NetworkSongList;
import com.example.win.easy.repository.web.download.PictureDownloadManager;
import com.example.win.easy.repository.web.download.SongDownloadManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Builder;

@Builder
public class SongListBatchSyncTask implements Runnable {

    private SongPojoDao songPojoDao;
    private SongListPojoDao songListPojoDao;
    private SongXSongListDao songXSongListDao;
    private SongDownloadManager songDownloadManager;
    private PictureDownloadManager pictureDownloadManager;
    private SongFactory songFactory;
    private AsyncTaskFactory asyncTaskFactory;

    private List<NetworkSongList> networkSongLists;

    private List<SongPojo> songLocalRecords;
    private List<NetworkSong> networkSongs;
    private List<Long> songIds;
    private List<Long> songListIds;
    private List<SongXSongList> relations;


    @Override
    public void run() {
        updateSongs();
        updateSongLists();
        updateSongXSongLists();
        initiateDownloadTask();
    }

    private void updateSongs(){
        //去重
        Set<NetworkSong> uniqueNetworkSongs=new HashSet<>();
        for (NetworkSongList networkSongList: networkSongLists)
            uniqueNetworkSongs.addAll(networkSongList.getNetworkSongs());
        for (NetworkSong networkSong:uniqueNetworkSongs){
            //查找本地记录
            SongPojo localRecord=songPojoDao.findLocalRecordOfNetworkSong(
                    networkSong.totalName,
                    networkSong.author,
                    networkSong.source.toString(),
                    networkSong.uid,
                    networkSong.remoteId);
            //没有本地记录时插入
            if (localRecord==null) {
                localRecord=songFactory.create(networkSong);
                songIds.add(songPojoDao.insert(localRecord));
            } else//有记录时记下id
                songIds.add(localRecord.id);
            //保留网络歌曲的引用
            networkSongs.add(networkSong);
            //保留本地歌曲的引用
            songLocalRecords.add(localRecord);
        }
    }

    private void updateSongLists(){
        for (NetworkSongList networkSongList: networkSongLists){
            //查找本地记录
            SongListPojo localRecord=songListPojoDao.findLocalRecordOfNetworkSongList(
                    networkSongList.name,
                    networkSongList.source.toString(),
                    networkSongList.uid,
                    networkSongList.remoteId);
            //没有本地记录时插入
            if (localRecord==null){
                localRecord=new SongListPojo(networkSongList);
                songListIds.add(songListPojoDao.insert(localRecord));
            }else//有记录时记下id
                songListIds.add(localRecord.id);
        }
    }

    private void updateSongXSongLists(){
        for (NetworkSongList networkSongList: networkSongLists){
            //获取歌单id
            long songListId=songListIds.get(networkSongLists.indexOf(networkSongList));
            List<NetworkSong> contents=networkSongList.networkSongs;
            for (NetworkSong networkSong:contents){
                //获取歌曲id
                long songId=songIds.get(networkSongs.indexOf(networkSong));
                //添加 歌曲-歌单 关系记录
                relations.add(new SongXSongList(songId,songListId));
            }
        }
        //插入关系数据
        songXSongListDao.insert(relations);
    }

    private void initiateDownloadTask(){
        int songAmount=songLocalRecords.size();
        for (int index=0;index<songAmount;index++){
            NetworkSong networkSong=networkSongs.get(index);
            long songId=songIds.get(index);
            songDownloadManager.download(asyncTaskFactory.create(networkSong,songId,SongDownloadTask.class));
            pictureDownloadManager.download(asyncTaskFactory.create(networkSong,songId,SongPictureDownloadTask.class));
        }

        int songListAmount=songListIds.size();
        for (int index=0;index<songListAmount;index++){
            NetworkSongList networkSongList= networkSongLists.get(index);
            long songListId=songListIds.get(index);
            pictureDownloadManager.download(new SongListPictureDownloadTask(networkSongList,songListId));
        }

    }
}
