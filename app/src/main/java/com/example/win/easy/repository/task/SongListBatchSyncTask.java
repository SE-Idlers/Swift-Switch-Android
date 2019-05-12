package com.example.win.easy.repository.task;

import com.example.win.easy.SwiftSwitchClassLoader;
import com.example.win.easy.repository.db.dao.SongListPojoDao;
import com.example.win.easy.repository.db.dao.SongPojoDao;
import com.example.win.easy.repository.db.dao.SongXSongListDao;
import com.example.win.easy.repository.db.database.OurDatabase;
import com.example.win.easy.repository.db.pojo.SongListPojo;
import com.example.win.easy.repository.db.pojo.SongPojo;
import com.example.win.easy.repository.db.pojo.SongXSongList;
import com.example.win.easy.repository.web.domain.NetworkSong;
import com.example.win.easy.repository.web.domain.NetworkSongList;
import com.example.win.easy.repository.web.download.PictureDownloadManager;
import com.example.win.easy.repository.web.download.SongDownloadManager;
import com.example.win.easy.thread.AppExecutors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SongListBatchSyncTask implements Runnable {

    private AppExecutors appExecutors;
    private SongListPojoDao songListPojoDao;
    private SongPojoDao songPojoDao;
    private SongXSongListDao songXSongListDao;
    private SongDownloadManager songDownloadManager=SongDownloadManager.getInstance();
    private PictureDownloadManager pictureDownloadManager=PictureDownloadManager.getInstance();

    private List<NetworkSongList> networkNewData;
    private List<SongPojo> songLocalRecords=new ArrayList<>();
    private List<NetworkSong> networkSongs=new ArrayList<>();
    private List<Long> songIds=new ArrayList<>();
    private List<Long> songListIds=new ArrayList<>();
    private List<SongXSongList> relations=new ArrayList<>();


    public SongListBatchSyncTask(List<NetworkSongList> networkNewData){
        this.networkNewData=networkNewData;
        appExecutors=AppExecutors.getInstance();
        OurDatabase ourDatabase= SwiftSwitchClassLoader.getOurDatabase();
        songListPojoDao=ourDatabase.songListPojoDao();
        songPojoDao=ourDatabase.songPojoDao();
        songXSongListDao=ourDatabase.songXSongListDao();
    }

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
        for (NetworkSongList networkSongList:networkNewData)
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
                localRecord=new SongPojo(networkSong);
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
        for (NetworkSongList networkSongList:networkNewData){
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
        for (NetworkSongList networkSongList:networkNewData){
            //获取歌单id
            long songListId=songListIds.get(networkNewData.indexOf(networkSongList));
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
            songDownloadManager.download(new SongDownloadTask(networkSong,songId));
            pictureDownloadManager.download(new SongPictureDownloadTask(networkSong,songId));
        }

        int songListAmount=songListIds.size();
        for (int index=0;index<songListAmount;index++){
            NetworkSongList networkSongList=networkNewData.get(index);
            long songListId=songListIds.get(index);
            pictureDownloadManager.download(new SongListPictureDownloadTask(networkSongList,songListId));
        }

    }
}
